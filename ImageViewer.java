import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Random;
import java.util.Scanner;

public class ImageViewer {
	BufferedImage img;
	int height;
	int width;
	Random rand;
	int percent;
	File f;
	int rounds;

    public static void main(String args[]) throws IOException
    {
        new ImageViewer();
    }

    //Setting up different files for testing
    public ImageViewer() throws IOException
    {
        //img = ImageIO.read(new File("kaleidoscope.bmp")); -- potentially corrupted
    	//img = ImageIO.read(new File("Sig.bmp"));
    	//f = new File("kaleidoscope.bmp");
    	f = new File("black.bmp");
    	//f = new File("white.bmp");
    	//f = new File("buckmural.bmp");
    	//f = new File("bmural5.bmp");
    	img = ImageIO.read(f);
        ImageIcon icon = new ImageIcon(img);
         
        rand = new Random();
        height = img.getHeight();
    	width = img.getWidth();
    	
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200,600);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //manipBlack();
        userManip();
    }
    
    
    //proof of concept method
    public void manipBlack() {
    	//int pixel = 0;
    	for (int h = 1; h < height; h++) {
    		for (int w = 1; w < width; w++) {
    			if (rand.nextInt(101) <= 2) {
    				img.setRGB(w, h, 0);
    			}
    		}
    	}
    }
    
    public void userManip() throws IOException {
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Type a Color: Red, Blue, Green, Gray, White, Black, Random, Super, or Gaussian");
    	int clr = 0;
    	String color = scan.next();
    	//need to check to make sure color matches before asking numbers
    	
    	System.out.println("Type a number between 1 and 100");
    	percent = scan.nextInt();
    	//Anything above 100 will just do 100%
    	
    	System.out.println("How many rounds do you want this to run?");
    	rounds = scan.nextInt();
    	//Technically no limit but be careful
    	
    	if (color.equals("Red")) {
    		clr = createColor(255, 0, 0).getRGB();
    	    //clr = red.getRGB();
    	}
    	
    	else if (color.equals("Blue")) {
    		clr = createColor(0, 0, 255).getRGB();
    	}
    	
    	else if (color.equals("Green")) {
    		clr = createColor(0, 255, 0).getRGB();
    	}
    	
    	else if (color.contentEquals("Gray")) {
    		clr = createColor(128, 128, 128).getRGB();
    	}
    	
    	else if (color.equals("Black")) {
    		clr = createColor(0, 0, 0).getRGB();
    	}
    	
    	else if (color.equals("White")) {
    		clr = createColor(255, 255, 255).getRGB();
    	}
    	
    	else if (color.equals("Random")) {
    		clr = createColor(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)).getRGB();
    	}
    	
    	else if (color.equals("Super")) {
    		superRandom();
    		return;
    	}
    	
    	else if (color.equals("Gaussian")) {
    		gaussian();
    		return;
    	}
    	
    	else {
    		System.out.println("Please follow all the guidelines");
    		return;
    	}
    	
    	for (int i = 1; i <= rounds; i++) {
    		for (int h = 0; h < height; h++) {
    			for (int w = 0; w < width; w++) {
    				if (rand.nextInt(101) <= percent) {
    					img.setRGB(w, h, clr);
    				}
    			}
    		}
    	}
    	ImageIO.write(img, "bmp", f); //Save the new image to original file
    		//Is there a way to save to a new file so original is not ruined?
    	System.out.println("Done");
    }
    
    //Creates a new color differently than previous method
    public void superRandom () throws IOException {
    	for (int i = 1; i <= rounds; i++) {
    		for (int h = 0; h < height; h++) {
    			for (int w = 0; w < width; w++) {
    				if (rand.nextInt(101) <= percent) {
    					int rgb = createColor(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)).getRGB();
    					//System.out.println(rgb);
    					img.setRGB(w, h, rgb);
    				}
    			}
    		}
    		ImageIO.write(img, "bmp", f);
    	}
    	System.out.println("Done");
    }
    
    public Color createColor(int r, int g, int b) {
    	Color color = new Color(r, g, b);
    	return color;
    }
    
    //Uses a normal distribution to gradually change color
    public void gaussian() {
    	//Color startingColor = createColor(128, 128, 128);
    	//img.setRGB(x, y, rgb);
    	//ImageIO.write(img, "bmp", f);
    	
    	int std = 5;
    	for (int i = 1; i <= rounds; i++) {
    		for (int h = 0; h < height; h++) {
    			for (int w = 0; w < width; w++) {
    				if (rand.nextInt(101) <= percent) {
    					Color color = new Color(img.getRGB(w, h));
    					int red = color.getRed();
    					int blue = color.getBlue();
    					int green = color.getGreen();
    					//System.out.println("red before change = " + red);
					
    					Double gaussRed = rand.nextGaussian() * std + red;
    					Double gaussBlue = rand.nextGaussian() * std + blue;
    					Double gaussGreen = rand.nextGaussian() * std + green;
    					//System.out.println("red after change = " + gaussRed);
    					int newRed = checkIfInBounds(gaussRed);
    					int newBlue = checkIfInBounds(gaussBlue);
    					int newGreen = checkIfInBounds(gaussGreen);
    					
    					Color newColor = new Color(newRed, newGreen, newBlue);
    					//Double gauss = rand.nextGaussian() * (std + color.getRGB());
    					//int intGauss = gauss.intValue();
    					img.setRGB(w, h, newColor.getRGB());
    					//System.out.println(intGauss);
    				}
    			}
    		}
    		//ImageIO.write(img, "bmp", f);
    		//System.out.println(i);
    	}

    	try {
    		File output = new File("C:\\Users\\Cole\\Downloads\\Fusion\\caesium-workspace\\BMP\\newimage.bmp");
			output.createNewFile();
    		ImageIO.write(img, "bmp", output);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to save image");
		}
    	System.out.println("Done");
    }
    
    //Separating and changing the r, g, and b values independently can cause out of bounds error
    //Makes sure color is between 0 and 255 and changes value to int
    public int checkIfInBounds(Double color) {
    	if (color > 255) {
			color = 255.0;
		}
    	else if (color < 0) {
    		color = 0.0;
    	}
		
		//System.out.println("red = " + red + " blue = " + blue + " green = " + green);
    	//Color newColor = new Color(red.intValue(), blue.intValue(), green.intValue());
    	int newColor = color.intValue();
    	return newColor;
    }
}
//It seems like if the program is interrupted while changing the pixels, i.e you close
//out the picture, there's a chance you'll corrupt the file and it will no longer be usable
