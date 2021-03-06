package com.eventfabric.example.products.ui;

import java.util.Random;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import com.eventfabric.api.client.EndPointInfo;
import com.eventfabric.api.client.EventClient;
import com.eventfabric.api.client.Response;
import com.eventfabric.api.model.Event;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextField;
import javax.swing.JCheckBox;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import org.apache.commons.codec.binary.StringUtils;

import java.util.List;
import java.util.ArrayList;

public class FrameProducts extends JFrame {
    private String host = "localhost";
    private int port = 8080;
	private EventClient client;
	private Timer taskTimer;
	private JPanel contentPane;
	private JPanel windowPane;
	private JTextField txtHost;
	private JLabel lblHost;
	private JTextField txtPort;
	private JLabel lblPort;
	private JLabel lblSecure;
	private JTextField txtUsername;
	private JCheckBox chSecure;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JPasswordField passwordField;
	private JTextField txtChannel;
	private JLabel lblChannel;
	private JTextArea lblResponseText;
	private JButton btnSend;
	private JButton btnPlay;
	private JLabel lblInterval;
	private JTextField txtInterval;
	private JLabel lblProducts;
	private JTextField txtProducts;
	private JButton btnConnect;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;

	/**
	 * Create the frame.
	 */
	public FrameProducts() {
		createLayout();
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connect();
			}
		});

		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendEvent();
			}
		});

		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				{
					if (taskTimer == null) {
						taskTimer = new Timer();
						TimerTask sendEventTimerTask = new TimerTask() {
							public void run() {
								sendEvent();
							}
						};
						long interval = Integer.parseInt(txtInterval.getText()) * 1000;
						if (interval < 1000) {
							interval = 1000;
						}
						taskTimer.scheduleAtFixedRate(sendEventTimerTask, 0,
								interval);
					} else {
						taskTimer.cancel();
						taskTimer = null;
					}

					if (btnPlay.getText() == "Play") {
						btnPlay.setText("Pause");
					} else {
						btnPlay.setText("Play");
					}

				}
			}
		});
	}

	private void sendEvent() {
		// if connect was not called, call it and check that it connects
		// successfully
		if (this.client != null || this.connect()) {
			LinkedHashMap<String, Object> value = new LinkedHashMap<String, Object>();
			Random generator = new Random();
			String[] products = txtProducts.getText().split(",");
			int i = generator.nextInt(products.length);
			value.put("product", products[i].trim());
			value.put("count", generator.nextInt(100));
			value.put("price", generator.nextFloat() * 100);
			value.put("delivered", Math.random() > 0.5);

			try {
				// create the event to send
				Event event = new Event(txtChannel.getText(), value);

                // provFrom
                String provFrom = "idqc://tijuana:8081?name=portals-access-by-day";
                List<String> provVia = new ArrayList<>();
                provVia.add("jdbc:oracle://localhost:1531/prod-db?table=baixas");
                provVia.add("kafka://localhost:9020/kafka-topic");
				// send the event
				Response response = client.send(event, provFrom, provVia);
				// do something with the response

				String responseText = String.format("Status: %s\nEvent:\n%s",
						response.getStatus(), event);
				lblResponseText.setText(responseText);
			} catch (IOException e) {
				lblResponseText.setText(e.getMessage());
			}
		}
	}

	private boolean connect() {
		EndPointInfo endPointInfo = new EndPointInfo(txtHost.getText(),
				"/streams", Integer.parseInt(txtPort.getText()), chSecure.isSelected());
		EndPointInfo sessionEndPointInfo = new EndPointInfo(txtHost.getText(),
				"/sessions", Integer.parseInt(txtPort.getText()), chSecure.isSelected());

		client = new EventClient(txtUsername.getText(),
				passwordField.getText(), endPointInfo, sessionEndPointInfo);
		try {
			boolean authenticated = client.authenticate();
			if (authenticated) {
				lblResponseText.setText("Connected");
				btnSend.setEnabled(true);
			} else {
				lblResponseText.setText("Not connected");
			}
			return authenticated;

		} catch (IOException e) {
			lblResponseText.setText(e.getMessage());
			return false;
		}
	}

	private void createLayout() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 400);
		setTitle("Event Fabric - Products - Agent example");
		windowPane = new JPanel();
		windowPane.setLayout(new BorderLayout(10, 10));
		setContentPane(windowPane);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(0, 3, 0, 0));

		JLabel picLabel = new JLabel();
		ImageIcon eventfabric = new ImageIcon("eventfabric.png");
		picLabel.setHorizontalAlignment(SwingConstants.CENTER); 
		picLabel.setIcon(eventfabric);  
		windowPane.add(picLabel, BorderLayout.PAGE_START);
		windowPane.add(contentPane, BorderLayout.CENTER);

		lblHost = new JLabel("Host:");
		lblHost.setLabelFor(txtHost);
		contentPane.add(lblHost);
		txtHost = new JTextField();
		txtHost.setText(host);
		contentPane.add(txtHost);
		txtHost.setColumns(10);

		label = new JLabel("");
		contentPane.add(label);

		lblPort = new JLabel("Port:");
		lblPort.setLabelFor(txtPort);
		contentPane.add(lblPort);
		txtPort = new JTextField();
		txtPort.setText(""+port);
		contentPane.add(txtPort);
		contentPane.add(new JLabel(""));

		lblSecure = new JLabel("Secure:");
		lblSecure.setLabelFor(chSecure);
		contentPane.add(lblSecure);
		chSecure = new JCheckBox();
		contentPane.add(chSecure);
		contentPane.add(new JLabel(""));

		lblUsername = new JLabel("Username:");
		lblUsername.setLabelFor(txtUsername);
		contentPane.add(lblUsername);
		txtUsername = new JTextField();
		txtUsername.setText("");
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);

		label = new JLabel("");
		contentPane.add(label);

		lblPassword = new JLabel("Password:");
		lblPassword.setLabelFor(passwordField);
		contentPane.add(lblPassword);
		passwordField = new JPasswordField();
		contentPane.add(passwordField);

		label_1 = new JLabel("");
		contentPane.add(label_1);

		lblChannel = new JLabel("Channel:");
		contentPane.add(lblChannel);
		txtChannel = new JTextField();
		txtChannel.setText("products");
		contentPane.add(txtChannel);
		txtChannel.setColumns(10);

		label_2 = new JLabel("");
		contentPane.add(label_2);

		lblInterval = new JLabel("Interval:");
		contentPane.add(lblInterval);

		txtInterval = new JTextField();
		txtInterval.setText("5");
		contentPane.add(txtInterval);
		txtInterval.setColumns(10);

		label_3 = new JLabel("(in seconds)");
		contentPane.add(label_3);

		lblProducts = new JLabel("Products:");
		contentPane.add(lblProducts);

		txtProducts = new JTextField();
		txtProducts.setText("ProductA,ProductB,ProductC");
		contentPane.add(txtProducts);
		txtProducts.setColumns(10);

		label_4 = new JLabel("(Separated by comma)");
		contentPane.add(label_4);

		btnConnect = new JButton("Connect");
		contentPane.add(btnConnect);

		btnPlay = new JButton("Play");
		contentPane.add(btnPlay);

		btnSend = new JButton("Send");
		contentPane.add(btnSend);

		lblResponseText = new JTextArea("");
		JScrollPane jScrollPane = new JScrollPane(lblResponseText);
		jScrollPane.setPreferredSize(new Dimension(0, 100));
		windowPane.add(jScrollPane, BorderLayout.PAGE_END);
	}
}
