import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

public class Player
{
    public static void main(String[] args)
    {
        MainFrame frame = new MainFrame();
        frame.setSize(400, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class MainFrame extends JFrame
{
    private JButton play;
    private JButton pause;
    private JButton stop;
    private JLabel label;
    private ImageIcon[] imageIcon;
    private JPanel panel;
    private int index;
    private Timer timer;

    private JButton queryplay;
    private JButton querypause;
    private JButton querystop;
    private JLabel querylabel;
    private ImageIcon[] queryIcon;
    private JPanel queryPanel;
    private int queryindex;
    private Timer querytimer;

    public MainFrame()
    {
        index = 1;
        imageIcon = new ImageIcon[600];
        for (int i = 0; i < 600; i++)
        {
            String xStr = String.format("%03d", i+1);
            String fileStr = "flowers" + xStr + ".rgb";
            imageIcon[i] = new ImageIcon(getImage(fileStr));
        }
        queryindex = 1;
        queryIcon = new ImageIcon[150];
        for (int i = 0; i < 150; i++)
        {
            queryIcon[i] = imageIcon[300 + i];
        }

        setLayout(new FlowLayout());

        play = new JButton("play");
        add(play);
        play.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                timer.start();
            }
        });

        pause = new JButton("pause");
        add(pause);
        pause.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                timer.stop();
            }
        });

        stop = new JButton("stop");
        add(stop);
        stop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                timer.stop();
                index = 1;
                label.setIcon(imageIcon[index-1]);
                // System.out.println(index);
            }
        });

        panel = new JPanel();
        label = new JLabel(imageIcon[0]);
        panel.add(label);
        add(panel);

        timer = new Timer(33, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                index++;
                label.setIcon(imageIcon[index]);
                // System.out.println(index);
                if(index >= 599)
                {
                    timer.stop();
                }
            }
        });

        queryplay = new JButton("queryplay");
        add(queryplay);
        queryplay.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                querytimer.start();
            }
        });

        querypause = new JButton("querypause");
        add(querypause);
        querypause.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                querytimer.stop();
            }
        });

        querystop = new JButton("querystop");
        add(querystop);
        querystop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event)
            {
                querytimer.stop();
                queryindex = 1;
                querylabel.setIcon(queryIcon[index-1]);
            }
        });

        queryPanel = new JPanel();
        querylabel = new JLabel(queryIcon[0]);
        queryPanel.add(querylabel);
        add(queryPanel);

        querytimer = new Timer(33, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                queryindex++;
                querylabel.setIcon(queryIcon[queryindex]);
                // System.out.println(queryindex);
                if(queryindex >= 149)
                {
                    querytimer.stop();
                }
            }
        });
    }

    public BufferedImage getImage(String fileName)
    {
        int width = 352;
        int height = 288;
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
}
