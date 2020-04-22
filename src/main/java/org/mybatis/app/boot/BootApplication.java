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

import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.add.ui.Button;
import org.mybatis.generator.add.ui.CheckBox;
import org.mybatis.generator.add.ui.CreatedConfigXml;
import org.mybatis.generator.add.ui.TextField;
import org.mybatis.generator.add.ui.beans.CommentGenerator;
import org.mybatis.generator.add.ui.beans.ContextBean;
import org.mybatis.generator.add.ui.beans.JavaClientGenerator;
import org.mybatis.generator.add.ui.beans.JavaModelGenerator;
import org.mybatis.generator.add.ui.beans.JavaTypeResolver;
import org.mybatis.generator.add.ui.beans.JdbcConnection;
import org.mybatis.generator.add.ui.beans.JdbcConnectionExt;
import org.mybatis.generator.add.ui.beans.SqlMapGenerator;
import org.mybatis.generator.add.ui.beans.Table;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.internal.util.CamelCaseUtils;
import org.shersfy.utils.LocalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootApplication extends JFrame implements ActionListener{
	
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
	
	public static String OUTPATH  = LocalConfig.getOutputPath();
	public static String CONF_XML = LocalConfig.getOutputConf();
	
	public static String PKG_XML	= LocalConfig.getPkgXml();
	public static String PKG_MODEL 	= LocalConfig.getPkgModel();
	public static String PKG_MAPPER = LocalConfig.getPkgMappger();
	public static String PKG_ROOT_CLASS = LocalConfig.getPkgRootClass();
	
	public static String JDBC_URL  = LocalConfig.getJdbcUrl();
	public static String JDBC_USER = LocalConfig.getJdbcUsername();
	public static String JDBC_PWD  = LocalConfig.getJdbcPassword();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BootApplication.class);
	private static ContextBean config;
	
	private JdbcConnectionExt conn;
	private Map<Integer, JPanel> mainPaneMap;
	
	//UI
	private JPanel mainPane;
	private JComboBox<String> dbCombo;
	private JTextField txtUrl;
	private JTextField txtUser;
	private JTextField txtPwd;
	private Table sampleTable;
	
	public BootApplication(String title){
		conn = new JdbcConnectionExt();
		conn.setDriverClass(LocalConfig.getJdbcDriver());
		
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
		dbCombo = new JComboBox<>();
		String[] arr = LocalConfig.getJdbcSupport().split(",");
		for(String dbname :arr) {
			dbCombo.addItem(dbname.trim());
		}
		dbCombo.setSelectedIndex(0);
		subPane1.add(dbCombo);
		
		// 连接URL
		JLabel lbUrl = new JLabel("连接URL");
		subPane1.add(lbUrl);
		txtUrl = new JTextField(TXT_LEN);
		txtUrl.setFont(FONT_SIZE);
		txtUrl.setText(JDBC_URL);
		subPane1.add(txtUrl);
		
		// 用户名
		JLabel lbUser = new JLabel("用户名");
		subPane1.add(lbUser);
		txtUser = new JTextField(TXT_LEN);
		txtUser.setFont(FONT_SIZE);
		txtUser.setText(JDBC_USER);
		subPane1.add(txtUser);
		
		// 密码
		JLabel lbPwd = new JLabel("密码");
		subPane1.add(lbPwd);
		txtPwd = new JTextField(TXT_LEN);
		txtPwd.setFont(FONT_SIZE);
		txtPwd.setText(JDBC_PWD);
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
				// none
			}
		});
		// 点击【连接】
		btnConn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				conn.setConnectionURL(txtUrl.getText());
				conn.setUserId(txtUser.getText());
				conn.setPassword(txtPwd.getText());
				if(connection(conn)){
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

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String appname = LocalConfig.getAppName();
		String version = LocalConfig.getAppVersion();
		if (StringUtils.isBlank(appname)) {
			throw new IOException("application name cannot be empty");
		}
		LOGGER.info("start application '{}'", appname);
		new BootApplication(appname+" "+version);
	}
	
	public static ContextBean getContextBean() {
		return config;
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
			conn.setConnectionURL(txtUrl.getText());
			conn.setUserId(txtUser.getText());
			conn.setPassword(txtPwd.getText());
			
			if(!connection(conn)){
				return;
			}
			
			JdbcConnection jdbcConnection = new JdbcConnection();
			jdbcConnection.setDriverClass(conn.getDriverClass());
			jdbcConnection.setConnectionURL(conn.getConnectionURL());
			jdbcConnection.setUserId(conn.getUserId());
			jdbcConnection.setPassword(conn.getPassword());
			
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
				tables[i].setDomainObjectName(CamelCaseUtils.toCamelCaseString(name.toLowerCase(), true));
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
			
			List<String> tbs = JdbcConnectionExt.getTables(conn);
			tables = tbs.toArray(new String[tbs.size()]);
			
		} catch (Exception e) {
			LOGGER.error("", e);
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
		subPanel.add(new JLabel("输出路径（初始化清空目录）"));
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
		rootClass.setText(PKG_ROOT_CLASS);
		subPanel.add(rootClass);
		
		subPanel.add(new JLabel("Model输出包名"));
		TextField modePackage = new TextField(TXT_LEN+10);
		modePackage.setId(22);
		modePackage.setFont(fo);
		modePackage.setText(PKG_MODEL);
		subPanel.add(modePackage);
		
		//第3组
		subPanel.add(new JLabel("XML输出包名"));
		TextField xmlPackage = new TextField(TXT_LEN+10);
		xmlPackage.setId(31);
		xmlPackage.setFont(fo);
		xmlPackage.setText(PKG_XML);
		subPanel.add(xmlPackage);
		
		//第4组
		subPanel.add(new JLabel("Mapper输出类型"));
		JComboBox<String> type = new JComboBox<String>();
		type.addItem("XMLMAPPER");
		type.addItem("MIXEDMAPPER");
		type.addItem("ANNOTATEDMAPPER");
		type.addItem("MAPPER");
		subPanel.add(type);
		
		subPanel.add(new JLabel("Mapper输出包名"));
		TextField daoPackage = new TextField(TXT_LEN+10);
		daoPackage.setId(41);
		daoPackage.setFont(fo);
		daoPackage.setText(PKG_MAPPER);
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
	
	public boolean connection(JdbcConnectionExt meta){
		boolean flg = true;
		try {
			DriverManager.getConnection(meta.getConnectionURL(), meta.getUserId(), meta.getPassword());
		} catch (Exception ex) {
			flg = false;
			LOGGER.error("", ex);
			JOptionPane.showMessageDialog(null, "连接异常\n"+ex.getMessage(), "异常", JOptionPane.ERROR_MESSAGE);
		}
		return flg;
	}
	

}
