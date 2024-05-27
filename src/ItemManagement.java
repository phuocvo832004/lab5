import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemManagement extends JFrame {

	private JPanel contentPane;
	private JTextField txtItemID;
	private JTextField txtItemName;
	private JTextField txtPrice;
	private JTable table;
	Connection con = null;
	DefaultTableModel model = new DefaultTableModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ItemManagement frame = new ItemManagement();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ItemManagement() {
		setTitle("Quản lý sản phẩm");
		

		String url = "jdbc:oracle:thin:@localhost:1521:orcllab5";
		String user = "c##lab5";
		String password = "123456";
		String driver = "oracle.jdbc.driver.OracleDriver";
		try {
		    Class.forName(driver);
		    con = DriverManager.getConnection(url, user, password);
		    System.out.println("Connected to the database");
		} catch (Exception e) {
		    e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 532, 513);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 496, 162);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Mã sản phẩm");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setBounds(10, 11, 130, 33);
		panel.add(lblNewLabel);
		
		txtItemID = new JTextField();
		txtItemID.setBounds(180, 11, 306, 33);
		panel.add(txtItemID);
		txtItemID.setColumns(10);
		
		JLabel lblTnSnPhm = new JLabel("Tên sản phẩm");
		lblTnSnPhm.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblTnSnPhm.setBounds(10, 55, 130, 33);
		panel.add(lblTnSnPhm);
		
		txtItemName = new JTextField();
		txtItemName.setColumns(10);
		txtItemName.setBounds(180, 55, 306, 33);
		panel.add(txtItemName);
		
		JLabel lblGi = new JLabel("Giá");
		lblGi.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblGi.setBounds(10, 99, 130, 33);
		panel.add(lblGi);
		
		txtPrice = new JTextField();
		txtPrice.setColumns(10);
		txtPrice.setBounds(180, 99, 306, 33);
		panel.add(txtPrice);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 403, 496, 60);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnAdd = new JButton("Thêm");
		btnAdd.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String id = txtItemID.getText();
		        String name = txtItemName.getText();
		        double price = 0;

		        try {
		            price = Double.parseDouble(txtPrice.getText());
		        } catch (NumberFormatException ex) {
		            JOptionPane.showMessageDialog(null, "Giá sản phẩm phải là một số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        if (id.isEmpty() || name.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Vui lòng nhập đủ thông tin sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        try {
		            Statement checkStatement = con.createStatement();
		            String checkSql = "SELECT * FROM item WHERE id = '" + id + "' OR name = '" + name + "'";
		            ResultSet resultSet = checkStatement.executeQuery(checkSql);
		            if (resultSet.next()) {
		                JOptionPane.showMessageDialog(null, "Sản phẩm đã tồn tại trong cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            Statement statement = con.createStatement();
		            String sql = "INSERT INTO item (id, name, price) VALUES ('" + id + "', '" + name + "', " + price + ")";
		            int rowsAffected = statement.executeUpdate(sql);
		            if (rowsAffected > 0) {
		                DefaultTableModel model = (DefaultTableModel) table.getModel();
		                model.addRow(new Object[]{id, name, price});
		                model.fireTableDataChanged();
		                txtItemID.setText("");
		                txtItemName.setText("");
		                txtPrice.setText("");
		                JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		            } else {
		                JOptionPane.showMessageDialog(null, "Thêm sản phẩm thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            }
		            statement.close();
		            con.close();
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});


		btnAdd.setBounds(10, 11, 89, 38);
		panel_2.add(btnAdd);
		
		JButton btnDelete = new JButton("Xóa");
		btnDelete.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int selectedRow = table.getSelectedRow();
		        if (selectedRow == -1) {
		            JOptionPane.showMessageDialog(null, "Vui lòng chọn một sản phẩm để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        String id = table.getValueAt(selectedRow, 0).toString();
		        
		        try {
		        	
		        	int result = JOptionPane.showOptionDialog(null, "Xác nhận xóa sản phẩm?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
		        	if(result == JOptionPane.YES_OPTION) {
		        		Statement statement = con.createStatement();
			            String sql = "DELETE FROM item WHERE id = '" + id + "'";
			            int rowsAffected = statement.executeUpdate(sql);
			            if (rowsAffected > 0) {
			                DefaultTableModel model = (DefaultTableModel) table.getModel();
			                model.removeRow(selectedRow);
			                model.fireTableDataChanged();
			                JOptionPane.showMessageDialog(null, "Xóa sản phẩm thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			            } else {
			                JOptionPane.showMessageDialog(null, "Xóa sản phẩm thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			            }
			            statement.close();
		        	}else {
		        		dispose();
		        	}
		        	con.close();
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		btnDelete.setBounds(142, 11, 89, 38);
		panel_2.add(btnDelete);
		
		JButton btnUpdate = new JButton("Sửa");
		btnUpdate.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int selectedRow = table.getSelectedRow();
		        if (selectedRow == -1) {
		            JOptionPane.showMessageDialog(null, "Vui lòng chọn một sản phẩm để sửa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        String id = table.getValueAt(selectedRow, 0).toString();
		        String name = table.getValueAt(selectedRow, 1).toString();
		        String priceString = table.getValueAt(selectedRow, 2).toString();
		        double price = Double.parseDouble(priceString);

		        String newName = JOptionPane.showInputDialog(null, "Nhập tên sản phẩm mới:", name);
		        String newPriceString = JOptionPane.showInputDialog(null, "Nhập giá sản phẩm mới:", price);
		        if (newName == null || newName.isEmpty() || newPriceString == null || newPriceString.isEmpty()) {
		            return;
		        }
		        double newPrice;
		        try {
		            newPrice = Double.parseDouble(newPriceString);
		        } catch (NumberFormatException ex) {
		            JOptionPane.showMessageDialog(null, "Giá sản phẩm phải là một số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        try {
		        	Statement statement = con.createStatement();
		        	int result = JOptionPane.showOptionDialog(null, "Xác nhận thay đổi?", "Confirm Patch", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
		        	if(result == JOptionPane.YES_OPTION) {
			            
			            String sql = "UPDATE item SET name = '" + newName + "', price = " + newPrice + " WHERE id = '" + id + "'";
			            int rowsAffected = statement.executeUpdate(sql);
			            if (rowsAffected > 0) {
			                DefaultTableModel model = (DefaultTableModel) table.getModel();
			                model.setValueAt(newName, selectedRow, 1);
			                model.setValueAt(newPrice, selectedRow, 2);
			                model.fireTableDataChanged();
			                JOptionPane.showMessageDialog(null, "Sửa sản phẩm thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

		        	}}
		             else {
		                JOptionPane.showMessageDialog(null, "Sửa sản phẩm thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		            }
		            statement.close();
		            con.close();
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		btnUpdate.setBounds(270, 11, 89, 38);
		panel_2.add(btnUpdate);
		
		JButton btnExit = new JButton("Thoát");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dispose();
			}
		});
		btnExit.setBounds(397, 11, 89, 38);
		panel_2.add(btnExit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 184, 496, 208);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.add(table);


		scrollPane.setViewportView(table);
		String[] columnNames = {"Mã sản phẩm", "Tên sản phẩm", "Giá"};
		model.setColumnIdentifiers(columnNames);
		table.setModel(model);
		searchData();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int row = table.getSelectedRow();
				
				txtItemID.setText(String.valueOf(table.getValueAt(row, 0)));
				txtItemName.setText(String.valueOf(table.getValueAt(row, 1)));
				txtPrice.setText(String.valueOf(table.getValueAt(row, 2)));
			}
		});


	}
	public void searchData() {
	    List<Item> items = new ArrayList<Item>();
	    try {
	        if (con == null || con.isClosed()) {
	            return;
	        }

	        String sql = "SELECT * FROM ITEM";
	        Statement statement = con.createStatement();
	        ResultSet result = statement.executeQuery(sql);

	        if (!result.isBeforeFirst()) {
	        }
	        
	        items.clear();
	        while (result.next()) {
	            int id = result.getInt("Id");
	            String name = result.getString("Name");
	            double price = result.getDouble("Price");
	            
	            Item item = new Item();
	            
	            item.setId(id);
	            item.setName(name);
	            item.setPrice(price);

	           
	            items.add(item);
	            
	        }
	        System.out.println("Items: " + items);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    for (Item item : items) {
	        Object[] o = new Object[3]; 
	        o[0] = item.getId();
	        o[1] = item.getName();
	        o[2] = item.getPrice();
	        model.addRow(o);
	    }
	}


	


}
