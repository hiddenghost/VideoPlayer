import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.*;
import javax.swing.event.*;

public class Player
{
    private final String querypath;
    private final String path;
    private final String cat;
    private final int windowNum;
    private final String[] display;
    public Player(String[] info, String[] displayInfo)
    {
        querypath = info[0];
        path = info[1];
        cat = info[2];
        windowNum = Integer.parseInt(info[3]);
        display = displayInfo;
    }

    public void play()
    {
        String fullpath = path + cat;
        int startposition = (windowNum - 1) * 30;
        MainFrame frame = new MainFrame(querypath, fullpath, startposition, display);
        frame.setSize(1200, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        String[] info = {"new queries/From Searching Content/Q7", "CSCI576_Project_Database/", "musicvideo", "10"};
        String[] display = {"movie", "94%", "flowers", "64%", "traffic", "34%"};
        Player videoPlayer = new Player(info, display);
        videoPlayer.play();
    }
}

class MainFrame extends JFrame
{
    private static final int MICROSECONDS_PER_SECOND = 1000000;
    private static final int MICROSECONDS_PER_MS = 1000;
    private static final int FRAME_DUR = 33;
    private static final int TOTAL_FRAME = 600;
    private static final int TOTAL_QUERY = 150;
    private static final int WIDTH = 352;
    private static final int HEIGHT = 288;
    private JButton play;
    private String path;// = "CSCI576_Project_Database/movie/movie";
    private JButton pause;
    private JButton stop;
    private JLabel label;
    private ImageIcon[] imageIcon;
    private JPanel panel;
    private int index;
    private Timer timer;
    private Clip clip;
    private long totalMs;

    private JButton queryplay;
    private String queryPath;
    private JButton querypause;
    private JButton querystop;
    private JLabel querylabel;
    private ImageIcon[] queryIcon;
    private JPanel queryPanel;
    private int queryindex;
    private Timer querytimer;
    private Clip queryclip;

    private Container container;
    private GridLayout gridLayout;
    private JSlider jSlider;
    private RectangleScore drawPanel;
    private int matchPoint;
    private String[] matchList;

    public MainFrame(String querypath, String fullpath, int startposition, String[] display)
    {
        super("Matched Player");
        matchPoint = startposition;
        matchList = display;
        String[] dataFullPath = getFullPath(fullpath);
        System.out.println(dataFullPath[0]);
        path = dataFullPath[1];
        index = startposition;
        imageIcon = new ImageIcon[TOTAL_FRAME];
        for (int i = 0; i < TOTAL_FRAME; i++)
        {
            String xStr = String.format("%03d", i + 1);
            String fileStr = path + xStr + ".rgb";
            imageIcon[i] = new ImageIcon(getImage(fileStr));
        }
        // clip = getAudio(path + ".wav");
        clip = getAudio(dataFullPath[0]);
        
        totalMs = clip.getMicrosecondLength();
        double audioPosition = index * totalMs / TOTAL_FRAME;
        long clipPosition = (long)audioPosition;
        clip.setMicrosecondPosition(clipPosition);
        // System.out.println(startposition);
        // System.out.println(totalMs);
        // System.out.println(clipPosition);

        queryindex = 0;
        queryIcon = new ImageIcon[TOTAL_QUERY];
        String[] queryFullPath = getFullPath(querypath);
        System.out.println(queryFullPath[0]);
        System.out.println(queryFullPath[1]);
        for (int i = 0; i < TOTAL_QUERY; i++)
        {
            // String xStr = String.format("_%03d", i + 1);
            String xStr = String.format("%03d", i + 1);
            String fileStr = queryFullPath[1] + xStr + ".rgb";
            queryIcon[i] = new ImageIcon(getImage(fileStr));
        }
        // queryclip = getAudio(querypath + ".wav");
        queryclip = getAudio(queryFullPath[0]);

        // setLayout(new BorderLayout( 5, 5 ));

        play = new JButton("play");
        play.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                timer.start();
                clip.start();
            }
        });

        pause = new JButton("pause");
        pause.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                timer.stop();
                clip.stop();
            }
        });

        stop = new JButton("stop");
        stop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                timer.stop();
                clip.stop();
                clip.setMicrosecondPosition(0 * MICROSECONDS_PER_SECOND);
                index = 0;
                label.setIcon(imageIcon[index]);
                jSlider.setValue(index);
                // System.out.println(index);
            }
        });

        timer = new Timer(FRAME_DUR, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                index++;
                label.setIcon(imageIcon[index]);
                if(index % 5 == 0)
                {
                    jSlider.setValue(index);
                }
                // jSlider.setValue(index);
                System.out.println(index);
                if(index >= TOTAL_FRAME - 1)
                {
                    timer.stop();
                    index = 0;
                    clip.setMicrosecondPosition(0 * MICROSECONDS_PER_SECOND);
                    clip.stop();
                }
            }
        });

        queryplay = new JButton("queryplay");
        queryplay.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                querytimer.start();
                queryclip.start();
            }
        });

        querypause = new JButton("querypause");
        querypause.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                querytimer.stop();
                queryclip.stop();
            }
        });

        querystop = new JButton("querystop");
        querystop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                querytimer.stop();
                queryclip.stop();
                queryindex = 0;
                queryclip.setMicrosecondPosition(0 * MICROSECONDS_PER_SECOND);
                querylabel.setIcon(queryIcon[queryindex]);
            }
        });

        querytimer = new Timer(FRAME_DUR, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                queryindex++;
                querylabel.setIcon(queryIcon[queryindex]);
                // System.out.println(queryindex);
                if(queryindex >= TOTAL_QUERY - 1)
                {
                    querytimer.stop();
                    queryindex = 0;
                    queryclip.setMicrosecondPosition(0 * MICROSECONDS_PER_SECOND);
                    queryclip.stop();
                }
            }
        });

        jSlider = new JSlider(SwingConstants.HORIZONTAL, 0, TOTAL_FRAME - 1, index);
        jSlider.setPreferredSize(new Dimension(300, 20));
        jSlider.addChangeListener(
            new ChangeListener()
            {
                public void stateChanged(ChangeEvent e)
                {
                    System.out.println(jSlider.getValue());
                    index = jSlider.getValue();
                    label.setIcon(imageIcon[index]);
                    double audioPosition = index * totalMs / TOTAL_FRAME;
                    long clipPosition = (long)audioPosition;
                    clip.setMicrosecondPosition(clipPosition);
                }
            }
        );

        gridLayout = new GridLayout(1, 3, 5, 5);
        container = getContentPane();
        // setLayout(new FlowLayout());
        setLayout(gridLayout);

        panel = new JPanel();
        // panel.setPreferredSize(new Dimension(400, 50));
        label = new JLabel(imageIcon[index]);
        panel.add(label);
        panel.add(play);
        panel.add(pause);
        panel.add(stop);
        panel.add(jSlider);
        add(panel);

        drawPanel = new RectangleScore();
        add(drawPanel);
        // drawPanel = new RectangleScore();
        // drawPanel.setPreferredSize(new Dimension(400, 50));
        // drawPanel.add(new RectangleScore());
        // panel.add(drawPanel);

        queryPanel = new JPanel();
        querylabel = new JLabel(queryIcon[0]);
        queryPanel.add(querylabel);
        queryPanel.add(queryplay);
        queryPanel.add(querypause);
        queryPanel.add(querystop);
        add(queryPanel);
    }

    public String[] getFullPath(String path)
    {
        String[] paths = new String[2];
        File file = new File(path);
        File[] files = file.listFiles();
        for (File i: files)
        {
            String cur = i.toString();
            if(cur.endsWith(".wav"))
                paths[0] = cur;
            if(cur.endsWith("001.rgb"))
            {
                int len = cur.length();
                paths[1] = cur.substring(0, len - 7);
            }
        }
        return paths;
    }

    public Clip getAudio(String fileName)
    {
        Clip clip;
        try
        {
            File inputFile = new File(fileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputFile);
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);
            return clip;
        }
        catch (UnsupportedAudioFileException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage getImage(String fileName)
    {
        int width = WIDTH;
        int height = HEIGHT;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        try
        {
            File file = new File(fileName);
            InputStream is = new FileInputStream(file);

            long len = file.length();
            byte[] bytes = new byte[(int)len];
            
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0)
            {
                offset += numRead;
            }
            int ind = 0;
            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    byte a = 0;
                    byte r = bytes[ind];
                    byte g = bytes[ind+height*width];
                    byte b = bytes[ind+height*width*2]; 
                    
                    int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                    img.setRGB(x,y,pix);
                    ind++;
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return img;
    }

    public class RectangleScore extends JPanel
    {
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.setColor(Color.GRAY);
            g.fillRect(50, 30, 300, 50);
            g.setColor(Color.BLACK);
            g.drawRect(50, 30, 300, 50);
            g.setColor(Color.RED);
            int x = (int)matchPoint * 300 / 600 + 50;
            int delty = 0;
            // g.drawLine(x, 35, x, 99);
            for (int i = 0; i < 75; i++)
            {
                if(i % 5 == 0)
                    delty ++;
                g.drawLine(x + i, 40 + delty, x + i, 79);
            }
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.setColor(Color.BLACK);
            g.drawString("Query: Query.rgb", 70, 120);
            g.drawRect(50, 100, 300, 30);
            g.drawRect(50, 140, 300, 120);
            g.setColor(Color.GRAY);
            g.fillRect(50, 170, 300, 25);
            g.setColor(Color.BLACK);
            g.drawRect(50, 170, 300, 25);
            g.drawString("Matched Videos:", 70, 160);
            g.drawString(matchList[0], 70, 190);
            g.drawString(matchList[1], 200, 190);
            g.drawString(matchList[2], 70, 215);
            g.drawString(matchList[3], 200, 215);
            g.drawString(matchList[4], 70, 240);
            g.drawString(matchList[5], 200, 240);
        }
    }
}
