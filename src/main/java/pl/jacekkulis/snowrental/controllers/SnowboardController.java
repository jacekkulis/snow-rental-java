package pl.jacekkulis.snowrental.controllers;


import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jacekkulis.snowrental.models.Snowboard;
import pl.jacekkulis.snowrental.services.ISnowboardService;

@Controller
public class SnowboardController {

	private static final Logger logger = LoggerFactory.getLogger(SnowboardController.class);

	@Autowired
	private ISnowboardService snowboardService;

	@RequestMapping("/snowboard")
	public String listSnowboard(Map<String, Object> map, HttpServletRequest request) {
		logger.info("list snowboard");
		int snowboardId = ServletRequestUtils.getIntParameter(request, "snowboardId", -1);

		if (snowboardId > 0)
			map.put("snowboard", snowboardService.findById(snowboardId));
		else
			map.put("snowboard", new Snowboard());

		map.put("snowboardList", snowboardService.findAll());

		return "item/snowboard";
	}
}