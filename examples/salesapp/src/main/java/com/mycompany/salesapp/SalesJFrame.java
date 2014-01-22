package com.mycompany.salesapp;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.eventfabric.api.client.Response;
import com.eventfabric.api.model.Event;
import com.mycompany.EventClient;

public class SalesJFrame extends JFrame {
	private JTextField txtPrice;
	private JTextField txtProduct;
	private JTextField txtResponse;
	private String efUsername;
	private String efPassword;

	public SalesJFrame(String efUsername, String efPassword) {
		this.efUsername = efUsername;
		this.efPassword = efPassword;
		
		this.setTitle("Sales App");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);                     
        setBounds(250, 200, 409, 205);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(4, 2));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(contentPane);
		
		// Product TextField
		txtProduct = new JTextField("");
		contentPane.add(new JLabel("Product:"));
		contentPane.add(txtProduct);
		
		// Price TextField
		txtPrice = new JTextField("");
		contentPane.add(new JLabel("Price:"));
		contentPane.add(txtPrice);

		// Save Button
		JButton btnSave = new JButton("Save");
		contentPane.add(new JLabel());
		contentPane.add(btnSave);
		
		// Response TextField
		txtResponse = new JTextField("");
		contentPane.add(new JLabel("Response:"));
		contentPane.add(txtResponse);
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String productName = txtProduct.getText();
				double price = Double.parseDouble(txtPrice.getText());
				
				Sale sale = new Sale();
				sale.setPrice(price);
				
				Product product = new Product();
				product.setName(productName);
				
				sale.setProduct(product);
				
				// save in database
				txtResponse.setText("Product was saved successfully");
				
				sendSaleToEventFabric(sale);
				
			}
		});
	}

	protected void sendSaleToEventFabric(Sale sale) {
		// TODO Auto-generated method stub
		EventClient eventClient = new EventClient(this.efUsername, this.efPassword);
		
		try {
			boolean authenticated = eventClient.authenticate();
			
			if (authenticated) {
				ObjectMapper mapper = new ObjectMapper();
				ObjectNode value = (ObjectNode) mapper.valueToTree(sale);
				Event event = new Event("sales", value );
				Response response = eventClient.send(event );
				
				if (response.getStatus() == 201) {
					txtResponse.setText("Sale sent to Event Fabric");
				} else {
					txtResponse.setText("Error sending Sale to Event Fabric");
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

