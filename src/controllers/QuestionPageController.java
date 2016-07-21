package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import json.JsonConverter;
import mappers.UserMapper;
import password.GmailSender;
import pojos.Error;
import pojos.Question;
import pojos.QuestionPage;
import pojos.Success;
import security.Encrypt;
import services.UserService;
import services.UserServiceImpl;

@RestController
public class QuestionPageController {
	@Autowired
	UserMapper usermapper;
	JsonConverter jconverter = new JsonConverter();
	Encrypt encrypter = new Encrypt();
	UserService userservice = new UserServiceImpl();
	@Autowired
	GmailSender emailSender;

	@RequestMapping(value = "/test/acceptance", method = RequestMethod.POST)
	public String handleLogout(HttpSession session) {
		//if (session.getAttribute("user") != null) {
			List<String> answers = new ArrayList<>();
			answers.add("yes");
			answers.add("no");
			
			Question question = new Question();
			question.setId(1);
			question.setDescription("desc1");
			question.setQuestionType("checkbox");
			question.setAnswers(answers);
			
			Question question2 = new Question();
			question.setId(2);
			question.setDescription("desc2");
			question.setQuestionType("checkbox");
			question.setAnswers(answers);
			
			List<Question> listOfQuests = new ArrayList<>();
			listOfQuests.add(question);
			listOfQuests.add(question2);
			QuestionPage questionpage = new QuestionPage();
			questionpage.setTestType("acceptance");
			questionpage.setQuestions(listOfQuests);
			
			
			Map<String,QuestionPage> jsonMap = new HashMap<>();
			jsonMap.put("tests",questionpage);
			String jsonString = jconverter.convertObjectToJsonString(jsonMap);
			System.out.println(jsonString);
			return jsonString;
		//}
		//return jconverter.convertObjectToJsonString(new Error("test", "You are not logged in."));
	}
}
