package haksa;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Haksa extends JFrame {
	// 데이터 베이스 연결
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	JTextField tfId = null;
	JTextField tfName = null;
	JTextField tfDept = null;
	JTextField tfAdress = null;
//	JTextArea taList = null;

	DefaultTableModel model = null;
	JTable table = null;

	public Haksa() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:myoracle", "ora_user", "woo");
			stmt = conn.createStatement();

		} catch (Exception e) {

			e.printStackTrace();
		}
		this.setTitle("학사관리");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if (rs != null) {rs.close();}
					if (conn != null) {conn.close();}
					if (stmt != null) {stmt.close();}
					if (pstmt != null) {pstmt.close();}
				} catch (Exception e1) {}
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}

		});

		Container c = getContentPane();
		c.setLayout(new FlowLayout());

		c.add(new JLabel("학번"));
		tfId = new JTextField(14);
		c.add(tfId);

		JButton btnSearch = new JButton("검색");
		c.add(btnSearch);
		// 검색 버튼 .-------------------------------------------------------------------
		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					rs = stmt.executeQuery("select * from student2 where id = '" + tfId.getText() + "'");
//					taList.setText("");
//					taList.append("==============================\n");
//					taList.append("학번       이름       학과       주소 \n");
//					taList.append("==============================\n");
//					while (rs.next()) {
//						taList.append(rs.getString("id") + "\t");
//						taList.append(rs.getString("name") + "\t");
//						taList.append(rs.getString("dept") + "\n");
//
//						tfName.setText(rs.getString("name"));
//						tfDept.setText(rs.getString("dept"));
//					}

					model.setNumRows(0);

					String[] row = new String[4];// 컬럼의 갯수가 3
					while (rs.next()) {

						row[0] = rs.getString("id");
						row[1] = rs.getString("name");
						row[2] = rs.getString("dept");
						row[3] = rs.getString("ADDRESS");
						model.addRow(row);
					}

				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}
		});

		// End--------------------------------------
		c.add(new JLabel("이름"));
		tfName = new JTextField(20);
		c.add(tfName);

		c.add(new JLabel("학과"));
		tfDept = new JTextField(20);
		c.add(tfDept);

		c.add(new JLabel("주소"));
		tfAdress = new JTextField(20);
		c.add(tfAdress);

//		taList = new JTextArea(14, 25);
//		JScrollPane sp = new JScrollPane(taList);
//		c.add(sp);

		String colName[] = { "학번", "이름", "학과", "주소" };
		model = new DefaultTableModel(colName, 0);
		table = new JTable(model);

		table.setPreferredScrollableViewportSize(new Dimension(250, 200));
		c.add(new JScrollPane(table));

		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				table = (JTable) e.getComponent();
				model = (DefaultTableModel) table.getModel();
				String id = (String) model.getValueAt(table.getSelectedRow(), 0);
				String name = (String) model.getValueAt(table.getSelectedRow(), 1);
				String dept = (String) model.getValueAt(table.getSelectedRow(), 2);
				String address = (String) model.getValueAt(table.getSelectedRow(), 3);

				tfId.setText(id);
				tfName.setText(name);
				tfDept.setText(dept);
				tfAdress.setText(address);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

		});

		JButton btnInsert = new JButton("등록");
		btnInsert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// ??? Query
					pstmt = conn.prepareStatement("insert into student2(id,name,dept,ADDRESS) values(?,?,?,?)");
					pstmt.setString(1, tfId.getText());
					pstmt.setString(2, tfName.getText());
					pstmt.setString(3, tfDept.getText());
					pstmt.setString(4, tfAdress.getText());
					pstmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "등록되었습니다.");
					totalList();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		});

		c.add(btnInsert);
		JButton btnList = new JButton("목록");
		// btnList버튼 이벤트
		// Start-----------------------------------------------------------------
		btnList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				totalList();
			}

		});

		// btnList버튼 이벤트
		// End-------------------------------------------------------------
		c.add(btnList);

		JButton updateBtn = new JButton("수정");
		updateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String idText = tfId.getText();
					stmt.executeUpdate(
							"update student2 set  name = '" + tfName.getText() + "', dept = '" + tfDept.getText()
									+ "', ADDRESS = '" + tfAdress.getText() + "'" + " where id = '" + idText + "'");

					JOptionPane.showMessageDialog(null, "수정되었습니다.");
					totalList();

				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		c.add(updateBtn);

		JButton deleBtn = new JButton("삭제");

		deleBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String idText = tfId.getText();
					System.out.println(idText);
					stmt.executeUpdate("delete from student2 where id = '" + idText + "'");

					JOptionPane.showMessageDialog(null, "삭제되었습니다.");
					totalList();
					tfId.setText("");
					tfName.setText("");
					tfDept.setText("");
				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}
		});

		c.add(deleBtn);

		this.setSize(300, 500);
		this.setVisible(true);
	}

	// 전체 리스트
	public void totalList() {
		try {
			rs = stmt.executeQuery("select * from student2");

			// JTable 초기화
			model.setNumRows(0);

			String[] row = new String[4];// 컬럼의 갯수가 3
			while (rs.next()) {
				row[0] = rs.getString("id");
				row[1] = rs.getString("name");
				row[2] = rs.getString("dept");
				row[3] = rs.getString("ADDRESS");
				model.addRow(row);
			}

		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new Haksa();
	}

}
