package pl.jacekkulis.snowrental.controllers;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jacekkulis.snowrental.models.Ski;
import pl.jacekkulis.snowrental.services.ISkiService;

@Controller
public class SkiController {

	private static final Logger logger = LoggerFactory.getLogger(SkiController.class);

	@Autowired
	private ISkiService skiService;

	@RequestMapping("/ski")
	public String listSki(Map<String, Object> map, HttpServletRequest request) {
		logger.info("list skis");
		int skiId = ServletRequestUtils.getIntParameter(request, "skiId", -1);

		if (skiId > 0)
			map.put("ski", skiService.findById(skiId));
		else
			map.put("ski", new Ski());

		map.put("skiList", skiService.findAll());

		return "item/ski";
	}
}