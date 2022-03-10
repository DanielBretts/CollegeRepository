import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.Select;


public class Program {

	public static void main(String[] args) throws AddressException, MessagingException, InterruptedException {
		JPanel panel = new JPanel();
		JTextField textField=new JTextField(12);
		JPasswordField passwordField=new JPasswordField(12);
		JPasswordField mailPasswordField = new JPasswordField(12); 
		JTextField tfMail = new JTextField();
		String[] semestersArr = {"1","2","3"};
		JComboBox semester = new JComboBox(semestersArr);
		String[] browsersArr = {"chrome","firefox","edge"};
		JComboBox browsers = new JComboBox(browsersArr);
		createView(panel, textField, passwordField,mailPasswordField,tfMail,semester,browsers);
		String username = textField.getText();
		String password = passwordField.getText();
		String passwordMail = mailPasswordField.getText();
		String browserChoice = browsers.getSelectedItem().toString();
		String email = tfMail.getText();
		String semesterChoice = semester.getSelectedItem().toString();
		System.out.println(password);
		System.out.println(username);
		System.out.println(password);
		System.out.println(passwordMail);
		System.out.println(email);
		System.out.println(browserChoice);
		System.out.println(semesterChoice);
//		while(passwordMail == null || !passwordMail.equals(Password.getPass()) ) {
//			JPanel jp = new JPanel();
//			createView(jp, textField, passwordField, mailPasswordField, tfMail, semester, browsers);
//		}
		openAfeka(username,password,passwordMail,email,semesterChoice,browserChoice);	
	}

	private static void createView(JPanel panel, JTextField textField, JPasswordField passwordField, JPasswordField mailPasswordField
			, JTextField tfMail, JComboBox semester, JComboBox browsers) {
		JFrame frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(false);
		panel.setLayout(new GridLayout(6,1));
		JLabel lbUsername=new JLabel("Username");
		JLabel lbPassword=new JLabel("Password");
		JLabel lbMailPassword=new JLabel("Mail for password");
		JLabel lbEmail=new JLabel("Enter your mail");
		JLabel lbSemester=new JLabel("Semester");
		JLabel lbBrowser=new JLabel("Browser");

		panel.add(lbUsername);
		panel.add(textField);
		panel.add(lbPassword);
		panel.add(passwordField);
		panel.add(lbMailPassword);
		panel.add(mailPasswordField);
		panel.add(lbEmail);
		panel.add(tfMail);
		panel.add(lbSemester);
		panel.add(semester);
		panel.add(lbBrowser);
		panel.add(browsers);
		int a=JOptionPane.showConfirmDialog(frame,panel,"Afeka Grade Sender",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
	}

	private static void openAfeka(String username, String password,String mailPassword,String email,String semester, String browserChoice) throws AddressException, MessagingException, InterruptedException {

		WebDriver driver = null;
		switch(browserChoice) {
		case "chrome":
		{
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
			break;
		}
		case "firefox":
		{
			System.setProperty("webdriver.gecko.driver", "drivers\\firefoxDriver.exe");
			driver = new FirefoxDriver();
			break;
		}
		case "edge":
		{
			System.setProperty("webdriver.edge.driver", "drivers\\msedgedriver.exe");
			driver = new EdgeDriver();
			break;
		}
		}
		driver.manage().window().maximize();
		driver.get("https://yedion.afeka.ac.il/yedion/fireflyweb.aspx?prgname=LoginValidation");
		WebElement user = driver.findElement(By.id("input_1"));
		user.sendKeys(username);
		By passwordLocator = RelativeLocator.with(By.tagName("input")).below(user);
		driver.findElement(passwordLocator).sendKeys(password);
		driver.findElement(By.xpath("//*[@id=\"submit_row\"]/td/input")).click();
		driver.findElement(By.xpath("//*[@id=\"#kt_header_menu\"]/div/div[3]/a/span")).click();
		driver.findElement(By.xpath("//*[@id=\"#kt_header_menu\"]/div/div[3]/div/div[5]/a/span")).click();
		Select semSelect = new Select(driver.findElement(By.id("R1C2")));
		switch(semester) {
			case "1":
			{
				semSelect.selectByIndex(1);
				break;
			}	
			case "2":
			{
				semSelect.selectByIndex(2);
				break;
			}
			case "3":
			{
				semSelect.selectByIndex(3);
				break;
			}
		}
		driver.findElement(By.xpath("//*[@id=\"kt_content\"]/div/div[2]/div/div/div/form/div[3]/a")).click();
		driver.findElement(By.xpath("//*[@id=\"IMG111\"]")).click();
		//load the courses status
		List<WebElement> allCourses = driver.findElements(By.tagName("b"));
		List<String> allCoursesStrings = new ArrayList<String>();
		for (int j = 0; j < allCourses.size(); j++) {
			allCoursesStrings.add(allCourses.get(j).getText());
		}
		//clicks again to open the semester section
		driver.findElement(By.xpath("//*[@id=\"kt_content\"]/div/div[2]/div/div/div/div[7]/div[2]/a/h2")).click();
		int i = 0;
		while (i == 0){
			driver.findElement(By.xpath("//*[@id=\"kt_content\"]/div/div[2]/div/div/div/div[7]/div[2]/a/h2")).click();
			List<WebElement> updatedElements = driver.findElements(By.tagName("b"));
			List<String> updatedCoursesStrings = new ArrayList<String>();
			for (int j = 0; j < updatedElements.size(); j++) {
				updatedCoursesStrings.add(updatedElements.get(j).getText());
				String str = updatedElements.get(j).getText();
				System.out.println(str);
			}
			System.out.println("=======================");
			for (int j = 0; j < updatedCoursesStrings.size(); j++) {
				if(!updatedCoursesStrings.get(i).equals(allCoursesStrings.get(i)))
				{
					WebElement weGrade = updatedElements.get(j);
					By byCourseName = RelativeLocator.with(By.tagName("h2")).above(weGrade);
					String courseName = driver.findElement(byCourseName).getText();
					String grade = updatedCoursesStrings.get(j);
					sendMail(grade, courseName,mailPassword,email);
					i=1;
					break;
				}
			}
			Thread.sleep(30000);
			driver.navigate().refresh();
		}

		driver.quit();
	}

	private static void sendMail(String grade,String courseName,final String password,String email) throws AddressException, MessagingException {
		System.out.println("===== SENDING MAIL =====");
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); //TLS
		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("danielsassontest@gmail.com", password);
			}
		});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("danielsassontest@gmail.com"));
		message.setRecipients(
				Message.RecipientType.TO, InternetAddress.parse(email));
		message.setSubject("קורס " + courseName);

		String msg = "קיבלת " + grade;

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);

		Transport.send(message);

		System.out.println("===== MAIL SENT =====");
	}	     
}






//		Email email = new SimpleEmail();
//		email.setHostName("smtp.office365.com");
//		email.setSmtpPort(995);
//		email.setAuthenticator(new DefaultAuthenticator("daniel.brettschneide@s.afeka.ac.il", pass));
//		email.setSSLOnConnect(true);
//		email.setFrom("daniel.brettschneide@s.afeka.ac.il");
//		email.setSubject("×¦×™×•×Ÿ ×‘ " + courseName);
//		email.setMsg("×§×™×‘×œ×ª " + grade);
//		email.addTo("daniel.brettschneide@s.afeka.ac.il");
//		email.send();
//		driver.findElement(By.xpath("//*[@id=\"target\"]/div[1]/div/div[1]/input")).sendKeys("daniel.brettschneide@s.afeka.ac.il");
//		driver.findElement(By.xpath("//*[@id=\"target\"]/div[2]/input")).sendKeys("×¦×™×•×Ÿ ×‘" + courseName);
//		driver.findElement(By.xpath("//*[@id=\"target\"]/div[3]/textarea")).sendKeys("×§×™×‘×œ×ª×™" + grade + "×‘×§×•×¨×¡" + courseName);



