package org.mybatis.app.boot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.apache.log4j.Logger;
import org.mybatis.generator.add.ui.AddMethodMapper;
import org.mybatis.generator.add.ui.Button;
import org.mybatis.generator.add.ui.CheckBox;
import org.mybatis.generator.add.ui.CreatedConfigXml;
import org.mybatis.generator.add.ui.TextField;
import org.mybatis.generator.add.ui.beans.CommentGenerator;
import org.mybatis.generator.add.ui.beans.ContextBean;
import org.mybatis.generator.add.ui.beans.DbMeta;
import org.mybatis.generator.add.ui.beans.JavaClientGenerator;
import org.mybatis.generator.add.ui.beans.JavaModelGenerator;
import org.mybatis.generator.add.ui.beans.JavaTypeResolver;
import org.mybatis.generator.add.ui.beans.JdbcConnection;
import org.mybatis.generator.add.ui.beans.SqlMapGenerator;
import org.mybatis.generator.add.ui.beans.Table;
import org.mybatis.generator.api.ShellRunner;

public class BootApplication extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Font FONT_SIZE = new Font(null, Font.BOLD, 20);
	
	private static final int MARGIN		 = 10;
	private static final int MARGIN_LEFT = 20;
	private static final int MARGIN_TOP  = 20;
	
	private static final int TXT_LEN  	= 18;
	
	private static final int ID_HOME  	= 0;
	private static final int ID_CONFIG  = 1;
	private static final int ID_OUTPUT  = 2;
	private static final int ID_TABLES  = 3;
	private static final int ID_CREATE  = 4;
	
	private static final String OUTPATH 		= "C:/mybatis/output";
	private static final String XML_PACKAGE		= "mapping";
	private static final String MODEL_PACKAGE 	= "org.shersfy.model";
	private static final String DAO_PACKAGE 	= "org.shersfy.mapper";
	private static final String ROOT_CLASS 		= "org.shersfy.model.BaseEntity";
	
	private static final String mysqlUrl 		= "jdbc:mysql://localhost:3306/dbname?useUnicode=true&characterEncoding=utf8&useSSL=false";
	private static final String oracleUrl 		= "jdbc:oracle:thin:@localhost:1521:dbname";
	private static final String msSQLUrl 		= "jdbc:sqlserver://localhost:1433;DatabaseName=dbname";
	
	
	
	private static Logger logger = Logger.getLogger(BootApplication.class);
	
	private ContextBean config;
	private Map<Integer, JPanel> mainPaneMap;
	
	//UI
	private JPanel mainPane;
	private JComboBox<DbMeta> dbCombo;
	private JTextField txtUrl;
	private JTextField txtUser;
	private JTextField txtPwd;
	private Table sampleTable;
	
	public BootApplication(String title){
		config = new ContextBean();
		mainPaneMap = new HashMap<Integer, JPanel>();
		this.setTitle(title);  
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setSize(800, 500);
		this.setLocationRelativeTo(null);

		mainPane = new JPanel();
		mainPane.setLayout(new FlowLayout(FlowLayout.LEFT, MARGIN_LEFT, MARGIN_TOP));
		
		JPanel subPane1 = new JPanel();
		subPane1.setSize(500,500);
		GridLayout grid = new GridLayout(0, 2, MARGIN, MARGIN);
		subPane1.setLayout(grid);
			
		// 数据库类型
		JLabel label = new JLabel("数据库类型");
		subPane1.add(label); 
		dbCombo = new JComboBox<DbMeta>();
		dbCombo.addItem(new DbMeta(DbMeta.MYSQL));
		dbCombo.addItem(new DbMeta(DbMeta.ORACLE));
		dbCombo.addItem(new DbMeta(DbMeta.MSSQL));
		dbCombo.setSelectedIndex(0);
		subPane1.add(dbCombo);
		
		// 连接URL
		JLabel lbUrl = new JLabel("连接URL");
		subPane1.add(lbUrl);
		txtUrl = new JTextField(TXT_LEN);
		txtUrl.setFont(FONT_SIZE);
		txtUrl.setText(mysqlUrl);
		subPane1.add(txtUrl);
		
		// 用户名
		JLabel lbUser = new JLabel("用户名");
		subPane1.add(lbUser);
		txtUser = new JTextField(TXT_LEN);
		txtUser.setFont(FONT_SIZE);
		//txtUser.setText("Username");
		subPane1.add(txtUser);
		
		// 密码
		JLabel lbPwd = new JLabel("密码");
		subPane1.add(lbPwd);
		txtPwd = new JTextField(TXT_LEN);
		txtPwd.setFont(FONT_SIZE);
		//txtPwd.setText("Password");
		subPane1.add(txtPwd);
		
		subPane1.add(new JLabel());
		subPane1.add(new JLabel());
		
		//连接
		JButton btnConn = new JButton("连接");
		
		subPane1.add(btnConn);
		//下一步
		Button btnNext = new Button("下一步");
		btnNext.setId(ID_HOME);
		subPane1.add(btnNext);
		
		// 选择数据库
		dbCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				DbMeta db = (DbMeta) e.getItem();
				switch (db.getTypeCode()) {
				case DbMeta.MYSQL:
					txtUrl.setText(mysqlUrl);
					break;
				case DbMeta.ORACLE:
					txtUrl.setText(oracleUrl);
					break;
				case DbMeta.MSSQL:
					txtUrl.setText(msSQLUrl);
					break;
				default:
					break;
				}
			}
		});
		// 点击【连接】
		btnConn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DbMeta db = (DbMeta) dbCombo.getSelectedItem();
				db.setConnectionURL(txtUrl.getText());
				db.setUserId(txtUser.getText());
				db.setPassword(txtPwd.getText());
				if(connection(db)){
					JOptionPane.showMessageDialog(null, "连接成功");
				}
			}
		});
		// 点击【下一步】
		btnNext.addActionListener(this);
		
		mainPaneMap.put(ID_HOME, subPane1);
		mainPane.add(subPane1);
		this.setContentPane(mainPane);
		

		this.setVisible(true);  
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		BootApplication ui = new BootApplication("Mybatis Generator");
	}
	
	@SuppressWarnings({"unchecked" })
	@Override
	public void actionPerformed(ActionEvent e) {

		Button next = (Button) e.getSource();
		
		JPanel nextPanel = null;
		JPanel subPane = (JPanel) mainPane.getComponents()[0];
		Component[] arr = subPane.getComponents();
		
		switch (next.getId()) {
		case ID_HOME:
			// jdbc配置操作
			DbMeta db = (DbMeta) dbCombo.getSelectedItem();
			db.setConnectionURL(txtUrl.getText());
			db.setUserId(txtUser.getText());
			db.setPassword(txtPwd.getText());
			
			if(!connection(db)){
				return;
			}
			
			JdbcConnection jdbcConnection = new JdbcConnection();
			jdbcConnection.setDriverClass(db.getDriverClass());
			jdbcConnection.setConnectionURL(db.getConnectionURL());
			jdbcConnection.setUserId(db.getUserId());
			jdbcConnection.setPassword(db.getPassword());
			
			config.setJdbcConnection(jdbcConnection);
			
			// UI操作
			nextPanel = createConfigPanel();
			break;
		case ID_CONFIG:
			// 输出项配置操作
			
			Map<Integer, CheckBox> checkBoxs = new HashMap<Integer, CheckBox>();
			for(Component c : arr){
				if(c instanceof CheckBox){
					CheckBox cb = (CheckBox) c;
					checkBoxs.put(cb.getId(), cb);
				}
			}
			// 注释配置
			CommentGenerator comment = new CommentGenerator();
			comment.setSuppressAllComments(!checkBoxs.get(Integer.valueOf(11)).isSelected());
			comment.setSuppressDate(!checkBoxs.get(Integer.valueOf(12)).isSelected());
			config.setCommentGenerator(comment);
			
			// JavaTypeResolver
			config.setJavaTypeResolver(new JavaTypeResolver(checkBoxs.get(Integer.valueOf(21)).isSelected()));
			
			boolean isSubPackages = checkBoxs.get(Integer.valueOf(31)).isSelected();
			
			JavaModelGenerator model = new JavaModelGenerator();
			model.setEnableSubPackages(isSubPackages);
			model.setTrimStrings(checkBoxs.get(Integer.valueOf(32)).isSelected());
			config.setJavaModelGenerator(model);
			
			SqlMapGenerator sqlMap = new SqlMapGenerator();
			sqlMap.setEnableSubPackages(isSubPackages);
			config.setSqlMapGenerator(sqlMap );
			
			JavaClientGenerator dao = new JavaClientGenerator();
			dao.setEnableSubPackages(isSubPackages);
			config.setJavaClientGenerator(dao);
			
			sampleTable = new Table();
			Map<String, Object> props = new HashMap<String, Object>();
			for(int id = 41; id<50; id++){
				if(checkBoxs.get(id) == null){
					break;
				}
				props.put(checkBoxs.get(id).getText(), checkBoxs.get(id).isSelected());
			}
			
			sampleTable.setProperties(props);
			
			// UI操作
			nextPanel = createOutputPanel();
			break;
			
		case ID_OUTPUT:
			// 输出表配置操作
			for(Component c : arr){
				if(c instanceof JComboBox<?>){
					JComboBox<?> type = (JComboBox<?>) c;
					config.getJavaClientGenerator().setType(
							String.valueOf(type.getSelectedItem()));
				}
				else if(c instanceof Button){
					Button output = (Button) c;
					//输出路径
					if(output.getId() == 11){
						config.getJavaModelGenerator().setTargetProject(output.getText());
						config.getSqlMapGenerator().setTargetProject(output.getText());
						config.getJavaClientGenerator().setTargetProject(output.getText());
					}
				}
				else if(c instanceof TextField){
					TextField txt = (TextField) c;
					int id = txt.getId();
					String content = txt.getText();
					switch (id) {
					case 21:
						config.getJavaModelGenerator().setRootClass(content);
						break;
					case 22:
						config.getJavaModelGenerator().setTargetPackage(content);
						break;
					case 31:
						config.getSqlMapGenerator().setTargetPackage(content);
						break;
					case 41:
						config.getJavaClientGenerator().setTargetPackage(content);
						break;

					default:
						break;
					}
				}
			}
			
			nextPanel = createTablesPanel();
			// UI操作
			break;
		case ID_CREATE:
			//创建配置文件
			Table[] tables = null;
			JList<String> list = null;
			JTextPane output = null;
			for(Component c : arr){
				if(c instanceof JScrollPane){
					list = (JList<String>) ((JScrollPane) c).getViewport().getView();
				}
				if(c instanceof JTextPane
						&& !((JTextPane) c).isEditable()){
					output = (JTextPane) c;
				}
			}
			
			List<String> tbList = list.getSelectedValuesList();
			tables = new Table[tbList.size()];
			
			for(int i=0; i<tables.length; i++){
				tables[i] = new Table();
				tables[i].setProperties(sampleTable.getProperties());
				String name = tbList.get(i);
				tables[i].setTableName(name);
				tables[i].setDomainObjectName(AddMethodMapper.underlineToCamel(name.toLowerCase()));
			}
			config.setTables(tables);
			if(tbList.isEmpty()){
				JOptionPane.showMessageDialog(this, "未选择表");
				return;
			}
			File xml = CreatedConfigXml.createConfigXml(config);
			if(xml!=null && xml.exists()){
				int res = JOptionPane.showConfirmDialog(this, "配置创建成功, 是否执行导出", 
						"执行导出", JOptionPane.YES_NO_OPTION);
				if(res == JOptionPane.YES_OPTION){
					ShellRunner.main(null);
					output.setText(config.getJavaModelGenerator().getTargetProject());
					// copy BaseEntity
					JOptionPane.showMessageDialog(this, "生成完毕");
				}
			}
			else{
				JOptionPane.showMessageDialog(this, "配置创建失败");
			}
			return;
		default:
			break;
		}
		
		mainPane.removeAll();
		int nextId = next.getId()+1;
		//下一步不是ID_TABLES界面
		if(mainPaneMap.containsKey(nextId) && nextId != ID_TABLES){
			//下一步已在列表
			mainPane.add(mainPaneMap.get(nextId));
		}
		else{
			//下一步不在列表
			mainPaneMap.put(nextId, nextPanel);
			mainPane.add(nextPanel);
		}
		
		this.setContentPane(mainPane);
		this.repaint();
		
	}
	
	/**选择输出表**/
	private JPanel createTablesPanel() {
		
		JPanel subPanel = new JPanel();
		subPanel.setSize(500,500);
		subPanel.setLayout(new GridLayout(0, 2, MARGIN, MARGIN));
		
		Font fo = new Font("", Font.BOLD, 14);
		// 第5组
		subPanel.add(new JLabel("选择生成表"));
		String[] tables = null;
		try {
			
			DbMeta meta = (DbMeta) dbCombo.getSelectedItem();
			List<String> tbs = DbMeta.getTables(meta);
			tables = tbs.toArray(new String[tbs.size()]);
			
		} catch (Exception e) {
			logger.error(e);
		}
		
		JList<String> list = new JList<String>(tables);
		list.setFont(fo);
		
		JScrollPane scrollPane = new JScrollPane(list);
		//scrollPane.setPreferredSize(new Dimension(150, 100));
		list.setVisibleRowCount(8);
		subPanel.add(scrollPane);
		
		subPanel.add(new JLabel());
		JTextPane output = new JTextPane();
		output.setLayout(new FlowLayout(FlowLayout.LEFT));
		output.setBackground(subPanel.getBackground());
		output.setEditable(false);
		output.setFont(fo);
		output.setPreferredSize(new Dimension(200, 40));
		subPanel.add(output);
		
		JTextPane txtPane1 = new JTextPane();
		txtPane1.setLayout(new FlowLayout(FlowLayout.LEFT));
		txtPane1.setBackground(subPanel.getBackground());
		
		Button btnPre = new Button("上一步");
		btnPre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPane.removeAll();
				mainPane.add(mainPaneMap.get(ID_OUTPUT));
				setContentPane(mainPane);
				repaint();
				
			}
		});
		txtPane1.add(btnPre);
		subPanel.add(txtPane1);
		
		JTextPane txtPane2 = new JTextPane();
		txtPane2.setLayout(new FlowLayout(FlowLayout.LEFT));
		txtPane2.setBackground(subPanel.getBackground());
		
		Button btnConfig = new Button("创建配置");
		btnConfig.setId(ID_CREATE);
		btnConfig.addActionListener(this);
		txtPane2.add(btnConfig);
		subPanel.add(txtPane2);
		
		return subPanel;
	}

	/**配置选项**/
	private JPanel createConfigPanel(){
		JPanel subPanel = new JPanel();
		subPanel.setSize(500,500);
		subPanel.setLayout(new GridLayout(0, 2, MARGIN, MARGIN));
		
		Font fo = new Font("", Font.BOLD, 14);
		
		//第1组
		CheckBox suppressAllComments = new CheckBox("是否生成注释");
		suppressAllComments.setFont(fo);
		suppressAllComments.setId(11);
		suppressAllComments.setSelected(true);
		subPanel.add(suppressAllComments);
		CheckBox suppressDate = new CheckBox("注释是否加日期时间");
		suppressDate.setId(12);
		suppressDate.setFont(fo);
		suppressDate.setSelected(true);
		subPanel.add(suppressDate);
		
		//第2组
		CheckBox forceBigDecimals = new CheckBox("forceBigDecimals");
		forceBigDecimals.setFont(fo);
		forceBigDecimals.setId(21);
		subPanel.add(forceBigDecimals);
		subPanel.add(new JLabel(""));
		
		//第3组
		CheckBox enableSubPackages = new CheckBox("是否根据package生成子目录");
		enableSubPackages.setId(31);
		enableSubPackages.setSelected(true);
		enableSubPackages.setFont(fo);
		subPanel.add(enableSubPackages);
		CheckBox trimStrings = new CheckBox("是否在生成的getter方法中调用trim(String类型)");
		trimStrings.setId(32);
		trimStrings.setFont(fo);
		subPanel.add(trimStrings);
		
		subPanel.add(new JLabel());
		subPanel.add(new JLabel());
		
		subPanel.add(new JLabel("生成方法配置"));
		subPanel.add(new JLabel());
		
		//第4组
		int id = 41;
		for(String key : Table.KEYS){
			CheckBox enableMethod = new CheckBox(key);
			enableMethod.setId(id);
			enableMethod.setFont(fo);
			subPanel.add(enableMethod);
			id++;
		}
		if(Table.KEYS.length%2 != 0){
			subPanel.add(new JLabel());
		}
		
		//上一步
		Button btnPre = new Button("上一步");
		btnPre.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPane.removeAll();
				mainPane.add(mainPaneMap.get(ID_HOME));
				setContentPane(mainPane);
				repaint();
			}
		});
		subPanel.add(btnPre);
		//下一步
		Button btnNext = new Button("下一步");
		btnNext.setId(ID_CONFIG);
		btnNext.addActionListener(this);
		subPanel.add(btnNext);
		
		return subPanel;
	}
	/**输出配置**/
	private JPanel createOutputPanel() {
		JPanel subPanel = new JPanel();
		subPanel.setSize(500,500);
		subPanel.setLayout(new GridLayout(0, 2, MARGIN, MARGIN));
		
		Font fo = new Font("", Font.BOLD, 14);
		
		//第1组
		subPanel.add(new JLabel("输出路径"));
		Button outpath = new Button();
		outpath.setFont(fo);
		outpath.setId(11);
		outpath.setText(OUTPATH);
		outpath.setBorder(BorderFactory.createLoweredBevelBorder());
		outpath.setBackground(Color.white);
		//文字左对齐
		outpath.setHorizontalAlignment(JButton.LEFT);
		outpath.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(outpath.getText());
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
				int res = fc.showDialog(null, "输出路径");
				if(res == JFileChooser.APPROVE_OPTION){
					File dir = fc.getSelectedFile();
					outpath.setText(dir.getPath());
				}
			}
		});
		subPanel.add(outpath);
		
		subPanel.add(new JLabel());
		subPanel.add(new JLabel());
		
		//第2组
		subPanel.add(new JLabel("Model基类(基类必须已导出到jar)"));
		TextField rootClass = new TextField(TXT_LEN+10);
		rootClass.setId(21);
		rootClass.setFont(fo);
		rootClass.setText(ROOT_CLASS);
		subPanel.add(rootClass);
		
		subPanel.add(new JLabel("Model输出包名"));
		TextField modePackage = new TextField(TXT_LEN+10);
		modePackage.setId(22);
		modePackage.setFont(fo);
		modePackage.setText(MODEL_PACKAGE);
		subPanel.add(modePackage);
		
		//第3组
		subPanel.add(new JLabel("Mapper输出包名"));
		TextField xmlPackage = new TextField(TXT_LEN+10);
		xmlPackage.setId(31);
		xmlPackage.setFont(fo);
		xmlPackage.setText(XML_PACKAGE);
		subPanel.add(xmlPackage);
		
		//第4组
		subPanel.add(new JLabel("Dao输出类型"));
		JComboBox<String> type = new JComboBox<String>();
		type.addItem("XMLMAPPER");
		type.addItem("MIXEDMAPPER");
		type.addItem("ANNOTATEDMAPPER");
		type.addItem("MAPPER");
		subPanel.add(type);
		
		subPanel.add(new JLabel("Dao输出包名"));
		TextField daoPackage = new TextField(TXT_LEN+10);
		daoPackage.setId(41);
		daoPackage.setFont(fo);
		daoPackage.setText(DAO_PACKAGE);
		subPanel.add(daoPackage);
		
		subPanel.add(new JLabel());
		subPanel.add(new JLabel());
		
		Button btnPre = new Button("上一步");
		btnPre.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPane.removeAll();
				mainPane.add(mainPaneMap.get(ID_CONFIG));
				setContentPane(mainPane);
				repaint();
				
			}
		});
		subPanel.add(btnPre);
		
		Button btnConfig = new Button("下一步");
		btnConfig.setId(ID_OUTPUT);
		btnConfig.addActionListener(this);
		subPanel.add(btnConfig);
		
		return subPanel;
	}
	
	public boolean connection(DbMeta meta){
		boolean flg = true;
		try {
			DriverManager.getConnection(meta.getConnectionURL(), meta.getUserId(), meta.getPassword());
		} catch (Exception ex) {
			flg = false;
			logger.error("", ex);
			JOptionPane.showMessageDialog(null, "连接异常\n"+ex.getMessage(), "异常", JOptionPane.ERROR_MESSAGE);
		}
		return flg;
	}
	

}
