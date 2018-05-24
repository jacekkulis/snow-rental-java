package pl.jacekkulis.snowrental.controllers;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.common.io.ByteSource;
import pl.jacekkulis.snowrental.models.Item;
import pl.jacekkulis.snowrental.services.IItemService;

@Controller
public class ItemController {

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private IItemService itemService;

	@RequestMapping(value = { "/itemImage" }, method = RequestMethod.GET)
	@Transactional
	public void getItemImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("code") String uniqueCode) throws IOException {
		logger.info("getItemImage");
		Item item = null;
		if (uniqueCode != null) {
			item = itemService.findByUniqueCode(uniqueCode);
		}
		
		Hibernate.initialize(item.getImage());

		if (item != null && item.getImage() != null) {
			InputStream inputStream = ByteSource.wrap(item.getImage().getBytes()).openStream();
			response.setContentType(MediaType.IMAGE_PNG_VALUE);
			// response.getOutputStream().write(ski.getImage());
			IOUtils.copy(inputStream, response.getOutputStream());
		}
		response.getOutputStream().close();
	}
}