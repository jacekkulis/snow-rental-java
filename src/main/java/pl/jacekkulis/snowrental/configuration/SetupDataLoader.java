package pl.jacekkulis.snowrental.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.jacekkulis.snowrental.dao.ImageRepository;
import pl.jacekkulis.snowrental.dao.RoleRepository;
import pl.jacekkulis.snowrental.dao.UserRepository;
import pl.jacekkulis.snowrental.models.*;
import pl.jacekkulis.snowrental.services.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(SetupDataLoader.class);

	private boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private ISkiService skiService;

	@Autowired
	private ISnowboardService snowboardService;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		if (alreadySetup) {
			return;
		}

		// == create initial roles
		final Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
		final Role userRole = createRoleIfNotFound("ROLE_USER");
		final Role servicemanRole = createRoleIfNotFound("ROLE_SERVICEMAN");

		// == create initial user
		User user = createUserIfNotFound("admin@gmail.com", "admin", "admin", "admin",
				new ArrayList<Role>(Arrays.asList(adminRole, userRole, servicemanRole)));
		createUserIfNotFound("user@gmail.com", "user", "user", "user", new ArrayList<Role>(Arrays.asList(userRole)));
		createUserIfNotFound("service@gmail.com", "service", "service", "service",
				new ArrayList<Role>(Arrays.asList(userRole, servicemanRole)));

		createMessageIfNotFound(user, "Welcome to our site " + user.getFirstName() + " " + user.getLastName(), MessageType.info);

		List<String> skiImageNames = new ArrayList<String>();
		skiImageNames.add("ski1");
		skiImageNames.add("ski2");
		skiImageNames.add("ski3");

		createSkiIfNotFound("Rossignol Experience 75",
				"Model nart EXPERIENCE 75 wyznacza standardy dla segmentu ALL MOUNTAIN dla średniozaawansowanych narciarzy. Narty znakomite na trasie, ale również świetnie spisują w lekkim terenie. Bardzo dobra kontrola,  trzymanie krawędzi  i zwrotność. Jazda staje się pozbawioną wysiłku przyjemnością.  30% teren/ 70% trasa, wiązania: LOOK X PRESS 10 B83",
				UUID.randomUUID().toString(), 175, 15, skiImageNames.get(0));
		createSkiIfNotFound("Rossignol Pursuit 300",
				"Wykorzystaj siłę i moc. Nowy model PURSUIT 300 jest przeznaczony na trasę i do dynamicznej jazdy skrętem carvingowym z dużą energią, precyzją dla zaawansowanego narciarza. Zapewniają przyczepność, precyzję, dynamikę i kontrolę. Szerokość 74 mm zapewnia stabilność i dość szybkie przejście z krawędzi na krawędź, co przy lekkiej karbonowej konstrukcji czyni jazdę przyjemną ale i nadaje jej sportowy charakter. ",
				UUID.randomUUID().toString(), 180, 16, skiImageNames.get(1));
		createSkiIfNotFound("Salomon XDR 80 Ti",
				"Zwycięzca w kategorii all mountain! Najlepsze z najlepszych nart! Salomon XDR 80 Ti porusza się płynnie i zwinnie, a jego prowadzenie nie wymaga dużego nakładu siły – przez cały dzień! Co więcej, narty wyróżniają się bardzo atrakcyjną, niebieską szatą graficzną, która w konkursie Snow Style nagrodzona została srebrnym medalem. Absolutnie polecamy zakup!",
				UUID.randomUUID().toString(), 176, 13, skiImageNames.get(2));

		List<String> snowboardImageNames = new ArrayList<String>();
		snowboardImageNames.add("snowboard1");
		snowboardImageNames.add("snowboard2");
		snowboardImageNames.add("snowboard3");

		createSnowboardIfNotFound("Burton LTR",
				"Made with Burton’s Easy Rider™ technology giving kids an incredibly soft and forgiving board that’s virtually impossible to catch an edge on. With a convex base that lifts the edges off the snow, reduced camber, and a super soft flex, these boards make it easy for lightweight riders to turn, stop, and ollie.",
				UUID.randomUUID().toString(), 176, "8", snowboardImageNames.get(0));
		createSnowboardIfNotFound("Burton Progression",
				"Take the fast track to the top. Speed Zone lacing gives you the power to get those boots on and off in seconds, and the increased pulling power makes finding that perfect fit quick and easy. No need to struggle with knots, torn laces, or loose boots - just hook, pull and tuck. ",
				UUID.randomUUID().toString(), 180, "5", snowboardImageNames.get(1));
		createSnowboardIfNotFound("Burton Process Flying V Experience",
				"The Burton Process Flying V is constantly being refined and tweaked but not much has changed when it comes to the overall ride and feel under foot to us over the years. For 2018 it’s good to see the weight rating relaxed a bit on the top end.  This review is for the 2015 Burton Process Flying V but a lot of this is still very applicable for the 2016-2018 models.",
				UUID.randomUUID().toString(), 190, "4.5", snowboardImageNames.get(2));

		alreadySetup = true;
	}

	@Transactional
	private final Role createRoleIfNotFound(final String roleName) {
		Role role = roleRepository.findByName(roleName);

		if (role == null) {
			role = new Role(roleName);
		}

		role = roleRepository.save(role);
		return role;
	}

	@Transactional
	private final User createUserIfNotFound(final String email, final String password, final String firstName,
			final String lastName, final List<Role> roles) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setPassword(passwordEncoder.encode(password));
			user.setEmail(email);
			user.setEnabled(true);
		}

		user.addRoles(roles);
		user = userRepository.save(user);
		return user;
	}

	@Transactional
	private final Ski createSkiIfNotFound(final String name, final String description, final String uniqueCode,
			final int size, final int radius, final String fileName) {

		Ski ski = skiService.findByName(name);

		if (ski == null) {
			ski = new Ski();
			ski.setName(name);
			ski.setDescription(description);
			ski.setUniqueCode(uniqueCode);
			ski.setSize(size);
			ski.setRadius(radius);

			byte[] imageBytes = getImageBytes("ski/" + fileName + ".png");

			if (imageBytes != null) {
				Image image = new Image();
				image.setBytes(imageBytes);
				imageRepository.save(image);
				ski.setImage(image);
			}
		}

		ski = skiService.create(ski);
		return ski;
	}

	@Transactional
	private final Snowboard createSnowboardIfNotFound(final String name, final String description,
			final String uniqueCode, final int size, final String flex, final String fileName) {

		Snowboard snowboard = snowboardService.findByName(name);

		if (snowboard == null) {
			snowboard = new Snowboard();
			snowboard.setName(name);
			snowboard.setDescription(description);
			snowboard.setUniqueCode(uniqueCode);
			snowboard.setSize(size);
			snowboard.setFlex(flex);

			byte[] imageBytes = getImageBytes("snowboard/" + fileName + ".png");

			if (imageBytes != null) {
				Image image = new Image();
				image.setBytes(imageBytes);
				imageRepository.save(image);
				snowboard.setImage(image);
			}
		}

		snowboard = snowboardService.create(snowboard);
		return snowboard;
	}

	private byte[] getImageBytes(String filePath) {
		InputStream inputStream = null;
		File file = new File("E:/Workspace/sts-workspace/SnowRental/src/main/webapp/resources/img/" + filePath);
		try {
			inputStream = FileUtils.openInputStream(file);
			logger.info("input stream not null");
			return IOUtils.toByteArray(inputStream);
		} catch (IOException err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
		}

		return null;
	}

	@Transactional
	private final Message createMessageIfNotFound(final User user, final String content,
			final MessageType messageType) {

		Message message = messageService.findByContent(content);
		if (message == null) {
			message = new Message();
			message.setContent(content);
			message.setMessageType(messageType);
			message.setRead(false);
			message.setUser(user);

		}
		
		return messageService.create(message);
	}
}