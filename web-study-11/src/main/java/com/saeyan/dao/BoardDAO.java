package com.saeyan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.saeyan.dto.BoardVO;

import util.DBManager;

public class BoardDAO {
	private BoardDAO() {

	}

	private static BoardDAO instance = new BoardDAO();

	public static BoardDAO getInstance() {
		return instance;
	}

	public List<BoardVO> selectAllBoards() {
		String sql = "select * from bboard order by num desc";

		List<BoardVO> list = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			// conn- Mysql연결 stmt- sql연결 rs-저장

			conn = DBManager.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			// next로 더 이상 읽을게 없을때까지 반복
			while (rs.next()) {

				// BoardVO 인스턴스가 밖에 있으면 저장된 가장 처음부분만 불러와서
				// sql문 첫번째 컬럼만 나오게 된다.
				BoardVO bVo = new BoardVO();

				// ResultSet에 SQL이 저장 되있는데 get으로 불러 온다.
				bVo.setNum(rs.getInt("num"));
				bVo.setName(rs.getString("name"));
				bVo.setEmail(rs.getString("email"));
				bVo.setPass(rs.getString("pass"));
				bVo.setTitle(rs.getString("title"));
				bVo.setContent(rs.getString("content"));
				bVo.setReadcount(rs.getInt("readcount"));
				bVo.setWritedate(rs.getTimestamp("writedate"));

				list.add(bVo); // list에 bVo 값 추가
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, stmt, rs); // 사용한 conn, stmt, rs 를 닫아준다.
		}

		return list; // bVo를 반복하여 담은 값(list)를 return(돌려준다)
	}

	// -----------------게시글 작성하기------------------
	public void insertBoard(BoardVO bVo) {
		String sql = "insert into bboard(" + "num, name, email, pass, title, content) "
				+ "values(board_seq.nextval, ?, ?, ?, ?, ?)";

		Connection conn = null; //// 초기화
		PreparedStatement pstmt = null;
		// Statement :SQL을 동작시키는데 Prepared(준비된)Statement는 ?로 값이 들어오는게 모를때 사용한다
		// Statement로 쓰는경우는 정확히 컬럼값이 주어져 있을때만 사용한

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql); // sql의 값을 넣어 연결한다.

			// //sql values에 ? 에 들어갈 순서 대로 BoardVO bVo로 받아온 값을 넣어준다.
			pstmt.setString(1, bVo.getName());
			pstmt.setString(2, bVo.getEmail());
			pstmt.setString(3, bVo.getPass());
			pstmt.setString(4, bVo.getTitle());
			pstmt.setString(5, bVo.getContent());

			pstmt.executeUpdate();// SQL을 담은 PreparedStatement를 실행한다.

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);// DBManager에서 끌어다 사용한 Connection과 Statement를 종료한다.
		}
	}

	//----------조회수-------------------------------
	public void updateReadCount(String num) {
		String sql = "update bboard set readcount= readcount+1 where num=?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, num);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
	}

	public BoardVO selectOneBoardByNum(String num) {
		String sql = "select * from bboard where num = ?";

		BoardVO bVo = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				bVo = new BoardVO();

				bVo.setNum(rs.getInt("num"));
				bVo.setName(rs.getString("name"));
				bVo.setEmail(rs.getString("email"));
				bVo.setPass(rs.getString("pass"));
				bVo.setTitle(rs.getString("title"));
				bVo.setContent(rs.getString("content"));
				bVo.setReadcount(rs.getInt("readcount"));
				bVo.setWritedate(rs.getTimestamp("writedate"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return bVo;
	}

	public void updateBoard(BoardVO bVo) {
		String sql = "update bboard set naem=?, email=?, pass=?, " + " title =?, content=? where num=?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bVo.getName());
			pstmt.setString(2, bVo.getEmail());
			pstmt.setString(3, bVo.getPass());
			pstmt.setString(4, bVo.getTitle());
			pstmt.setString(5, bVo.getContent());
			pstmt.setInt(5, bVo.getNum());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
	}

	public BoardVO checkPassWord(String pass, String num) {
		String sql = "SELECT * FROM BBOARD WHERE PASS =? and NUM?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVO bVo = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, pass);
			pstmt.setString(2, num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				bVo = new BoardVO();

				bVo.setNum(rs.getInt("num"));
				bVo.setName(rs.getString("name"));
				bVo.setEmail(rs.getString("email"));
				bVo.setPass(rs.getString("pass"));
				bVo.setTitle(rs.getString("title"));
				bVo.setContent(rs.getString("content"));
				bVo.setReadcount(rs.getInt("readcount"));
				bVo.setWritedate(rs.getTimestamp("writedate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bVo;
	}

	public void deleteBoard(String num) {
		String sql = "DELETE BBOARD WHERE NUM=?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, num);
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}