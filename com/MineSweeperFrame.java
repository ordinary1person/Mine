
////import 扫雷.musicStuff;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
////import java.nio.channels.NoConnectionPendingException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
////import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
////import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
//导入flac解码包jflac.jar
//import org.kc7bfi.jflac.apps.Player;

public class MineSweeperFrame extends JFrame {
  // 游戏模式编号
  public static final int Custom_Mode = 0;
  public static final int Elementary_Mode = 1;
  public static final int Intermediate_Mode = 2;
  public static final int Advanced_Mode = 3;
  // 游戏模式参数
  private static final int[] Elementary = { 9, 9, 10, Elementary_Mode };
  private static final int[] Intermediate = { 16, 16, 40, Intermediate_Mode };
  private static final int[] Advanced = { 16, 30, 99, Advanced_Mode };
  // 游戏字体、图片、文字颜色
  private static final Font FontC = new Font("Consolas", Font.PLAIN, 22);
  private static final Font FontD = new Font("等线", Font.PLAIN, 20);
  private static final ImageIcon imgicon = new ImageIcon(
      "icon.jpg");
  private static final ImageIcon imgflag = new ImageIcon(
      "flag.jpg");
  private static final ImageIcon imgbomb = new ImageIcon(
      "bomb.jpg");
  private static final Color[] NumberColor = { Color.blue,
      Color.green, Color.orange, Color.magenta,
      Color.red, Color.cyan, Color.black, Color.gray };
  // 数据文件及高分记录
  private static File file = new File("MineSweeper.txt");
  private static int HighScore_Elementary;
  private static int HighScore_Intermediate;
  private static int HighScore_Advanced;
  // private static final String files = "C:\\Users\\35317\\Desktop\\扫雷\\大风吹.wav";

  private int gamemode;
  private int row;
  private int column;
  private int area;
  private int minecount;
  private int flagcount;
  private int enablecount;
  private boolean[][] minearea;
  private boolean[][] flagmark;
  private boolean isGameOver;
  private boolean isFirstClick;
  private TimeThread timeThread;

  private JPanel contentPane;
  private JMenuBar menuBar;
  private JPanel pnlCount;
  private JPanel pnlMine;
  private MineButton[][] btnMine;
  private JTextField txtTime;
  private JTextField txtCount;

  /**
   * Launch the application.
   * 
   * @throws LineUnavailableException
   * @throws IOException
   */
  public static void main(String[] args) throws IOException, LineUnavailableException {
    // 音乐播放
    // new Player().decode(files);
    String filepath = "大风吹.wav";
    musicStuff musicObject = new musicStuff();
    musicObject.playMusic(filepath);
    // try {
    // new Player().decode(files);
    // } catch (Exception ex) {
    // ex.printStackTrace();
    // }

    int[] i = getDataFromFile();
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          new MineSweeperFrame(i[0], i[1], i[2], i[3]);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public MineSweeperFrame(int row, int column, int minecount, int mode) {
    // 设置各组件的默认字体
    UIManager.put("Menu.font", FontD);
    UIManager.put("Label.font", FontD);
    UIManager.put("Button.font", FontD);
    UIManager.put("MenuBar.font", FontD);
    UIManager.put("MenuItem.font", FontD);
    UIManager.put("TextField.font", FontD);
    UIManager.put("OptionPane.buttonFont", FontD);
    UIManager.put("OptionPane.messageFont", FontD);

    setTitle("扫 雷");
    setResizable(false); // 设置窗口不可改变大小
    setVisible(true); // 设置窗口初始可见
    setIconImage(imgicon.getImage()); // 设置窗体的icon
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // 注册监听器，响应窗口关闭事件
    addWindowListener(new WindowAdapter() {

      public void windowClosing(WindowEvent e) {
        saveDataToFile();
      }
    });

    menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mGame = new JMenu("游戏");
    menuBar.add(mGame);

    JMenuItem mRestart = new JMenuItem("重新开始");
    mRestart.addActionListener(new RestartListener());
    mGame.add(mRestart);

    // JMenuItem mitRestart = new JMenuItem("重新开始本局");
    // mitRestart.addActionListener(new RestartListener());
    // mRestart.add(mitRestart);

    // JMenuItem mitReset = new JMenuItem("开始新的游戏");
    // mitReset.addActionListener(new ResetMineListener());
    // mRestart.add(mitReset);

    JMenu mDifficulty = new JMenu("难度设置");
    mGame.add(mDifficulty);

    JMenuItem mitElementary = new JMenuItem("初级");
    mitElementary.addActionListener(new ResetModeListener(Elementary));
    mDifficulty.add(mitElementary);

    JMenuItem mitIntermediate = new JMenuItem("中级");
    mitIntermediate.addActionListener(new ResetModeListener(Intermediate));
    mDifficulty.add(mitIntermediate);

    JMenuItem mitAdvanced = new JMenuItem("高级");
    mitAdvanced.addActionListener(new ResetModeListener(Advanced));
    mDifficulty.add(mitAdvanced);

    JMenuItem mitCustom = new JMenuItem("自定义");
    mitCustom.addActionListener(new CustomSizeFrameListener());
    mDifficulty.add(mitCustom);

    JMenuItem mitHighScore = new JMenuItem("高分榜");
    mitHighScore.addActionListener(new HighScoreListener());
    mGame.add(mitHighScore);

    JMenu mHelp = new JMenu("帮助");
    mHelp.add(new JMenuItem("http://www.baidu.com/"));
    menuBar.add(mHelp);

    JMenuItem mMaker = new JMenuItem("制作组");
    mMaker.addActionListener(new MakerListener());
    mGame.add(mMaker);

    //// JMenu myMaker = new JMenu("制作人员");
    //// myMaker.addActionListener(new MakerListener());
    //// mMaker.add(new JMenuItem("侯诗新"));
    //// mMaker.add(myMaker);

    mGame.addSeparator();
    JMenuItem mitExit = new JMenuItem("退出");
    // 注册监听器，监听“退出”菜单项
    mitExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveDataToFile();
        System.exit(0);
      }
    });
    mGame.add(mitExit);

    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(contentPane);

    pnlCount = new JPanel();
    contentPane.add(pnlCount, BorderLayout.NORTH);

    JLabel lblTime = new JLabel("时间");
    pnlCount.add(lblTime);

    txtTime = new JTextField("0", 3);
    txtTime.setEditable(false);
    pnlCount.add(txtTime);

    JLabel lblCount = new JLabel("计数");
    pnlCount.add(lblCount);

    txtCount = new JTextField("0/" + minecount, 5);
    txtCount.setEditable(false);
    pnlCount.add(txtCount);

    pnlMine = new JPanel();
    contentPane.add(pnlMine, BorderLayout.CENTER);

    gamemode = mode;
    setMineArea(row, column, minecount);
    setMinePosition();
    setMineButton();
    UIManager.put("Label.font", FontC);
  }

  // 从文件中读取数据
  public static int[] getDataFromFile() {
    try {
      FileInputStream fis = new FileInputStream(file);
      byte[] buffer = new byte[256];
      int size = fis.read(buffer);
      fis.close();
      String[] s = new String(buffer, 0, size).split(" ");
      HighScore_Elementary = Integer.parseInt(s[0]);
      HighScore_Intermediate = Integer.parseInt(s[1]);
      HighScore_Advanced = Integer.parseInt(s[2]);
      int[] im = { Integer.parseInt(s[3]), Integer.parseInt(s[4]),
          Integer.parseInt(s[5]), Integer.parseInt(s[6]) };
      return im;
    } catch (Exception e) {
      HighScore_Elementary = Integer.MAX_VALUE;
      HighScore_Intermediate = Integer.MAX_VALUE;
      HighScore_Advanced = Integer.MAX_VALUE;
      return Elementary;
    }
  }

  // 保存数据到文件
  public void saveDataToFile() {
    try {
      String s = HighScore_Elementary + " " + HighScore_Intermediate + " "
          + HighScore_Advanced + " " + row + " " + column + " "
          + minecount + " " + gamemode;
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(s.getBytes());
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 初始化面板的各项参数
  private void setMineArea(int row, int column, int minecount) {
    setSize(30 * column + 20, 30 * row + 120);
    // 设置窗口在屏幕的正中心
    setLocationRelativeTo(null);
    this.row = row;
    this.column = column;
    this.minecount = minecount;
    area = row * column;
    flagcount = 0;
    enablecount = area;
    minearea = new boolean[row][column];
    flagmark = new boolean[row][column];
    isGameOver = false;
    isFirstClick = true;
  }

  // 随机生成地雷的位置
  private void setMinePosition() {
    // 生成minecount个不重复的随机数
    int[] pos = new int[area];
    for (int i = 0; i < area; i++) {
      pos[i] = i;
    }
    Random rand = new Random();
    for (int i = 0; i < minecount; i++) {
      int n = rand.nextInt(area - i);
      minearea[pos[n] / column][pos[n] % column] = true;
      pos[n] = pos[area - i - 1];
    }
  }

  // 向地雷面板上添加按钮
  private void setMineButton() {
    pnlMine.setLayout(new GridLayout(row, column));
    btnMine = new MineButton[row][column];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < column; j++) {
        btnMine[i][j] = new MineButton(i, j);
        pnlMine.add(btnMine[i][j]);
      }
    }
  }

  // “重新开始本局”菜单项监听类
  private class RestartListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      new MenuFunction().Restart();
    }
  }

  //// “开始新的的游戏”菜单项监听类
  //// private class ResetMineListener implements ActionListener {
  //// public void actionPerformed(ActionEvent e) {
  //// new MenuFunction().ResetMine();
  //// }
  //// }

  // “游戏模式设置”菜单项监听类
  private class ResetModeListener implements ActionListener {
    private int[] mode;

    public ResetModeListener(int[] mode) {
      this.mode = mode;
    }

    public void actionPerformed(ActionEvent e) {
      if (gamemode == mode[3]) {
        new MenuFunction().ResetMine();
      } else {
        new MenuFunction().ResetSize(mode[0], mode[1], mode[2]);
        gamemode = mode[3];
      }
    }
  }

  // “自定义”菜单项监听类，JFrame窗口的子类
  private class CustomSizeFrameListener extends JFrame implements ActionListener {
    public CustomSizeFrameListener() {
      setTitle("自定义");
      setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      setSize(320, 220);
      setLocationRelativeTo(null); // 设置窗口在屏幕的正中心
      setResizable(false); // 设置窗体不可改变大小
      JPanel contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane.setLayout(new GridLayout(4, 0));
      setContentPane(contentPane);

      JPanel pnlRow = new JPanel();
      contentPane.add(pnlRow);
      pnlRow.add(new JLabel("行数："));
      JTextField txtRow = new JTextField(10);
      pnlRow.add(txtRow);

      JPanel pnlColumn = new JPanel();
      contentPane.add(pnlColumn);
      pnlColumn.add(new JLabel("列数："));
      JTextField txtColumn = new JTextField(10);
      pnlColumn.add(txtColumn);

      JPanel pnlMineCount = new JPanel();
      contentPane.add(pnlMineCount);
      pnlMineCount.add(new JLabel("雷数："));
      JTextField txtMineCount = new JTextField(10);
      pnlMineCount.add(txtMineCount);

      JPanel pnlButton = new JPanel();
      contentPane.add(pnlButton);
      JButton btnOK = new JButton("确定");
      btnOK.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            int r = Integer.parseInt(txtRow.getText().trim());
            int c = Integer.parseInt(txtColumn.getText().trim());
            int count = Integer.parseInt(txtMineCount.getText().trim());
            if (r <= 0 || c <= 0 || r > 24 || c > 30) {
              JOptionPane.showMessageDialog(null, "限制行数：1~24\n限制列数：1~30",
                  "警告", JOptionPane.WARNING_MESSAGE);
              return;
            }
            if (count <= 0 || count > r * c) {
              JOptionPane.showMessageDialog(null, "限制雷数：1~" + (r * c),
                  "警告", JOptionPane.WARNING_MESSAGE);
              return;
            }
            new MenuFunction().ResetSize(r, c, count);
            gamemode = Custom_Mode;
            setVisible(false);
          } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "无效输入！",
                "错误", JOptionPane.ERROR_MESSAGE);
          }
        }
      });
      pnlButton.add(btnOK);
    }

    public void actionPerformed(ActionEvent e) {
      setVisible(true);
    }
  }

  // “高分榜”菜单项监听类
  private class HighScoreListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(null, "初级：" + HighScore_Elementary
          + "秒\n中级：" + HighScore_Intermediate
          + "秒\n高级：" + HighScore_Advanced + "秒",
          "高分榜", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  // “制作组”菜单项监听类
  private class MakerListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(null, "第一人员："
          + "侯诗新\n第二人员："
          + "XXX\n第三人员：" + "XXX",
          "制 作 人 员", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  // 菜单栏事件监听函数类
  private class MenuFunction {
    // “重新开始本局”函数
    public void Restart() {
      flagcount = 0;
      enablecount = area;
      if (!isFirstClick) {
        timeThread.interrupt();
        isFirstClick = true;
        txtTime.setText("0");
      }
      txtCount.setText("0/" + minecount);
      for (int i = 0; i < row; i++) {
        for (int j = 0; j < column; j++) {
          flagmark[i][j] = false;
          btnMine[i][j].removeLabel();
          btnMine[i][j].setIcon(null);
          btnMine[i][j].setOriginalStyle();
          btnMine[i][j].setEnabled(true);
        }
      }
      if (isGameOver) {
        for (int i = 0; i < row; i++) {
          for (int j = 0; j < column; j++) {
            btnMine[i][j].setMineListener();
          }
        }
        isGameOver = false;
      }
    }

    // “开始新的游戏”函数
    public void ResetMine() {
      minearea = new boolean[row][column];
      setMinePosition();
      Restart();
    }

    // “游戏模式设置”函数
    public void ResetSize(int r, int c, int count) {
      Restart();
      txtCount.setText("0/" + count);
      pnlMine.removeAll();
      setMineArea(r, c, count);
      setMinePosition();
      setMineButton();
    }
  }

  // 地雷区按钮监听类
  private class MineListener extends MouseAdapter {
    private int r;
    private int c;
    private boolean bothpress;
    private MouseFunction mousefunction;

    public MineListener(int r, int c) {
      this.r = r;
      this.c = c;
      mousefunction = new MouseFunction(r, c);
    }

    // 鼠标点击时触发
    public void mousePressed(MouseEvent e) {
      // 判断是否为左右双键同时点击
      int d = e.getModifiersEx();
      if (d == InputEvent.BUTTON1_DOWN_MASK + InputEvent.BUTTON3_DOWN_MASK) {
        bothpress = true;
      } else {
        bothpress = false;
      }
      if (bothpress) {
        mousefunction.bothPressed();
      }
    }

    // 鼠标释放时触发
    public void mouseReleased(MouseEvent e) {
      if (bothpress) {
        mousefunction.bothReleased();
      } else if (e.getButton() == MouseEvent.BUTTON1) {
        mousefunction.leftReleased(r, c);
      } else if (e.getButton() == MouseEvent.BUTTON3 && !bothpress) {
        mousefunction.rightReleased();
      }
      if (minecount == enablecount) {
        mousefunction.Success();
      }
    }
  }

  // 地雷区鼠标点击事件监听函数类
  private class MouseFunction {
    private int r;
    private int c;
    private boolean[] bool;

    public MouseFunction(int r, int c) {
      this.r = r;
      this.c = c;
      bool = surroundingButtons(r, c);
    }

    // 判断被点击按钮周边3*3范围内有无其它按钮
    private boolean[] surroundingButtons(int r, int c) {
      boolean[] bool = { true, true, true, true, true, true, true, true, true };
      if (r == 0) {
        bool[0] = bool[1] = bool[2] = false;
      }
      if (r == row - 1) {
        bool[6] = bool[7] = bool[8] = false;
      }
      if (c == 0) {
        bool[0] = bool[3] = bool[6] = false;
      }
      if (c == column - 1) {
        bool[2] = bool[5] = bool[8] = false;
      }
      return bool;
    }

    // 左键释放函数
    public void leftReleased(int r, int c) {
      if (isFirstClick) {
        timeThread = new TimeThread();
        timeThread.start();
        isFirstClick = false;
      }
      if (flagmark[r][c] || !btnMine[r][c].isEnabled()) {
        return;
      }
      if (minearea[r][c]) {
        GameOver(r, c);
        return;
      }

      boolean[] bool = surroundingButtons(r, c);
      int i = 0;
      if (bool[0] && minearea[r - 1][c - 1]) {
        i++;
      }
      if (bool[1] && minearea[r - 1][c]) {
        i++;
      }
      if (bool[2] && minearea[r - 1][c + 1]) {
        i++;
      }
      if (bool[3] && minearea[r][c - 1]) {
        i++;
      }
      if (bool[5] && minearea[r][c + 1]) {
        i++;
      }
      if (bool[6] && minearea[r + 1][c - 1]) {
        i++;
      }
      if (bool[7] && minearea[r + 1][c]) {
        i++;
      }
      if (bool[8] && minearea[r + 1][c + 1]) {
        i++;
      }

      btnMine[r][c].setDisabledStyle();
      btnMine[r][c].setEnabled(false);
      enablecount--;
      if (i != 0) {
        btnMine[r][c].setLabel(i);
      } else {
        if (bool[0]) {
          leftReleased(r - 1, c - 1);
        }
        if (bool[1]) {
          leftReleased(r - 1, c);
        }
        if (bool[2]) {
          leftReleased(r - 1, c + 1);
        }
        if (bool[3]) {
          leftReleased(r, c - 1);
        }
        if (bool[5]) {
          leftReleased(r, c + 1);
        }
        if (bool[6]) {
          leftReleased(r + 1, c - 1);
        }
        if (bool[7]) {
          leftReleased(r + 1, c);
        }
        if (bool[8]) {
          leftReleased(r + 1, c + 1);
        }
      }
    }

    // 右键释放函数
    public void rightReleased() {
      if (flagmark[r][c]) {
        btnMine[r][c].setIcon(null);
        flagmark[r][c] = false;
        flagcount--;
        txtCount.setText(flagcount + "/" + minecount);
      } else {
        if (btnMine[r][c].isEnabled()) {
          btnMine[r][c].setIcon(imgflag);
          flagmark[r][c] = true;
          flagcount++;
          txtCount.setText(flagcount + "/" + minecount);
        }
      }
    }

    // 双键点击函数
    public void bothPressed() {
      if (flagmark[r][c]) {
        return;
      }
      int k = 0;
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          if (bool[k++] && btnMine[r + i][c + j].isEnabled() && !flagmark[r + i][c + j]) {
            btnMine[r + i][c + j].setClickedStyle();
          }
        }
      }
    }

    // 双键释放函数
    public void bothReleased() {
      if (flagmark[r][c]) {
        return;
      }
      int k = 0, m = 0, n = 0;
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          if (bool[k++]) {
            if (btnMine[r + i][c + j].isEnabled() && !flagmark[r + i][c + j]) {
              btnMine[r + i][c + j].setOriginalStyle();
            }
            if (minearea[r + i][c + j]) {
              m++;
            }
            if (flagmark[r + i][c + j]) {
              n++;
            }
          }
        }
      }
      if (btnMine[r][c].isEnabled()) {
        return;
      }
      if (m == n) {
        if (bool[0] && !flagmark[r - 1][c - 1]) {
          leftReleased(r - 1, c - 1);
        }
        if (bool[1] && !flagmark[r - 1][c]) {
          leftReleased(r - 1, c);
        }
        if (bool[2] && !flagmark[r - 1][c + 1]) {
          leftReleased(r - 1, c + 1);
        }
        if (bool[3] && !flagmark[r][c - 1]) {
          leftReleased(r, c - 1);
        }
        if (bool[5] && !flagmark[r][c + 1]) {
          leftReleased(r, c + 1);
        }
        if (bool[6] && !flagmark[r + 1][c - 1]) {
          leftReleased(r + 1, c - 1);
        }
        if (bool[7] && !flagmark[r + 1][c]) {
          leftReleased(r + 1, c);
        }
        if (bool[8] && !flagmark[r + 1][c + 1]) {
          leftReleased(r + 1, c + 1);
        }
      }
    }

    // 游戏结束函数
    public void GameOver(int r, int c) {
      btnMine[r][c].setBackground(Color.red);
      for (int i = 0; i < row; i++) {
        for (int j = 0; j < column; j++) {
          if (minearea[i][j]) {
            btnMine[i][j].setIcon(imgbomb);
          }
          btnMine[i][j].removeMineListener();
        }
      }
      timeThread.interrupt();
      JOptionPane.showMessageDialog(null, "Game Over",
          "提示", JOptionPane.INFORMATION_MESSAGE);
      isGameOver = true;
    }

    // 游戏胜利函数
    public void Success() {
      txtCount.setText(minecount + "/" + minecount);
      for (int i = 0; i < row; i++) {
        for (int j = 0; j < column; j++) {
          if (minearea[i][j]) {
            btnMine[i][j].setIcon(imgflag);
          }
          btnMine[i][j].removeMineListener();
        }
      }
      if (timeThread != null) {
        timeThread.interrupt();
      }
      String s = "You Win!";
      int time = Integer.parseInt(txtTime.getText());
      switch (gamemode) {
        case Elementary_Mode:
          if (time < HighScore_Elementary) {
            HighScore_Elementary = time;
            s = "New Record!";
          }
          break;
        case Intermediate_Mode:
          if (time < HighScore_Intermediate) {
            HighScore_Intermediate = time;
            s = "New Record!";
          }
          break;
        case Advanced_Mode:
          if (time < HighScore_Advanced) {
            HighScore_Advanced = time;
            s = "New Record!";
          }
      }
      JOptionPane.showMessageDialog(null, s + "\n用时：" + time + "秒",
          "提示", JOptionPane.INFORMATION_MESSAGE);
      isGameOver = true;
    }
  }

  // 地雷区按钮类，JButton按钮的子类
  private class MineButton extends JButton {
    private MineListener mineListener;
    private JLabel lbl;

    private MineButton() {
      super(null, null);
      setBackground(Color.lightGray);
      // 设置凸起来的按钮
      setBorder(BorderFactory.createRaisedBevelBorder());
    }

    public MineButton(int r, int c) {
      this();
      mineListener = new MineListener(r, c);
      addMouseListener(mineListener);
    }

    public void setMineListener() {
      addMouseListener(mineListener);
    }

    public void removeMineListener() {
      removeMouseListener(mineListener);
    }

    public void setOriginalStyle() {
      setBackground(Color.lightGray);
      // 设置凸起来的按钮
      setBorder(BorderFactory.createRaisedBevelBorder());
    }

    public void setDisabledStyle() {
      setBackground(null);
      // 设置按钮边框线条
      setBorder(BorderFactory.createLineBorder(Color.lightGray));
    }

    public void setClickedStyle() {
      setBackground(Color.lightGray);
      // 设置凹下去的按钮
      setBorder(BorderFactory.createLoweredBevelBorder());
    }

    public void setLabel(int i) {
      lbl = new JLabel(String.valueOf(i));
      lbl.setHorizontalAlignment(JLabel.CENTER);
      lbl.setVerticalAlignment(JLabel.CENTER);
      lbl.setForeground(NumberColor[i - 1]);
      setLayout(new BorderLayout(0, 0));
      add(lbl, BorderLayout.CENTER);
    }

    public void removeLabel() {
      if (lbl != null) {
        remove(lbl);
        lbl = null;
      }
    }
  }

  // 线程类，游戏开始后每隔一秒刷新txtTime文本框里显示的时间
  private class TimeThread extends Thread {
    // 重写run()方法
    public void run() {
      // 获取当前系统时间
      long startTime = System.currentTimeMillis();
      // 非阻塞过程中通过判断中断标志来退出
      while (!isInterrupted()) {
        // 获取当前的系统时间，与初始时间相减就是程序运行的毫秒数，除以1000就是秒数
        long endTime = System.currentTimeMillis();
        long usedTime = (endTime - startTime) / 1000;
        txtTime.setText(String.valueOf(usedTime));
        try {
          // 线程挂起一秒钟
          Thread.sleep(1000);
          // 阻塞过程捕获中断异常来退出
        } catch (InterruptedException e) {
          e.printStackTrace();
          // 捕获到异常之后，执行break跳出循环
          break;
        }
      }
    }
  }
}