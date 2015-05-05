import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

public class VideoPlayer
{
    // public static void displayImg(BufferedImage img, JFrame frame)
    // {
    //     JPanel panel = new JPanel();
    //     panel.add(new JLabel(new ImageIcon(img)));
        
    //     frame.getContentPane().add(panel);
    //     frame.pack();
    //     frame.setVisible(true);
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // }

    public static void main(String[] args)
    {
        // System.out.println("kit kat");
        // JFrame frame = new JFrame();
        // JButton button = new JButton("Change");
        // button.setBounds(20, 20, 100, 20);
        // frame.add(button);
        // BufferedImage img = getImage("flowers001.rgb");
        // BufferedImage img2 = getImage("flowers600.rgb");
        // JPanel panel = new JPanel();
        // panel.add(new JLabel(new ImageIcon(img)));
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class MainFrame extends JFrame
{
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

    public MainFrame()
    {
        setSize(400, 400);
         
        flag = false;
         
        panel1 = new JPanel();
        label1 = new JLabel(new ImageIcon(getImage("flowers300.rgb")));
        panel1.add(label1);
         
        panel2 = new JPanel();
        label2 = new JLabel(new ImageIcon(getImage("flowers001.rgb")));
        panel2.add(label2);
         
        add(panel1);
         
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem item = new JMenuItem("Change panel");

        menu.add(item);
        menuBar.add(menu);
        setJMenuBar(menuBar);
         
        item.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
                if (flag)
                {
                    flag = false;
                    MainFrame.this.remove(panel2);
                    MainFrame.this.add(panel1);
                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    MainFrame.this.validate();
                    MainFrame.this.repaint();
                }
                else
                {
                    flag = true;
                    MainFrame.this.remove(panel1);
                    MainFrame.this.add(panel2);
                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    MainFrame.this.validate();
                    MainFrame.this.repaint();
                }
            }
         });
         
    }
     
    private boolean flag;
    private JPanel panel1, panel2;
    private JLabel label1, label2;
}