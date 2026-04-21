package finalProject.StatWeaver.serviceImpl;

import finalProject.StatWeaver.dto.CharacterDto;
import finalProject.StatWeaver.dto.CreateCharacterFromGenerationRequest;
import finalProject.StatWeaver.dto.StartGenerationResponse;
import finalProject.StatWeaver.dto.QuestionDto;
import finalProject.StatWeaver.mapper.CharacterMapper;
import finalProject.StatWeaver.mapper.QuestionMapper;
import finalProject.StatWeaver.model.Answer;
import finalProject.StatWeaver.model.Character;
import finalProject.StatWeaver.model.Question;
import finalProject.StatWeaver.repository.AnswerRepository;
import finalProject.StatWeaver.repository.CharacterRepository;
import finalProject.StatWeaver.repository.QuestionRepository;
import finalProject.StatWeaver.service.CharacterClassifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CharacterGenerationServiceImpl implements finalProject.StatWeaver.service.CharacterGenerationService {

    private static final int MAX_TOTAL_POINTS = 300;

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CharacterRepository characterRepository;
    private final QuestionMapper questionMapper;
    private final CharacterMapper characterMapper;

    private final CharacterClassifier classifier = new CharacterClassifier();

    public CharacterGenerationServiceImpl(
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            CharacterRepository characterRepository,
            QuestionMapper questionMapper,
            CharacterMapper characterMapper
    ) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.characterRepository = characterRepository;
        this.questionMapper = questionMapper;
        this.characterMapper = characterMapper;
    }

    @Override
    public StartGenerationResponse getQuestionsForGeneration(int questionCount) {
        if (questionCount <= 0) {
            throw new IllegalArgumentException("questionCount must be > 0");
        }

        long totalQuestions = questionRepository.count();
        if (totalQuestions <= 0) {
            throw new IllegalStateException("No questions in DB. Seed Question/Answer first.");
        }

        int count = (int) Math.min(questionCount, totalQuestions);
        List<QuestionDto> questionDtos = questionRepository.findAll().stream()
                .sorted(Comparator.comparing(Question::getId))
                .limit(count)
                .map(questionMapper::toDto)
                .toList();

        StartGenerationResponse response = new StartGenerationResponse();
        response.setQuestions(questionDtos);
        response.setQuestionCount(questionDtos.size());
        return response;
    }

    @Override
    public CharacterDto createCharacter(CreateCharacterFromGenerationRequest request) {
        Map<Long, Long> chosen = request.getSelectedAnswersByQuestionId();
        if (chosen == null || chosen.isEmpty()) {
            throw new IllegalArgumentException("You must answer at least one question.");
        }

        int intelligence = 0;
        int strength = 0;
        int defense = 0;
        int agility = 0;
        int magic = 0;

        for (Map.Entry<Long, Long> entry : chosen.entrySet()) {
            Long qId = entry.getKey();
            Long answerId = entry.getValue();
            if (answerId == null) {
                throw new IllegalArgumentException("Null answer for questionId=" + qId);
            }

            Answer answer = answerRepository.findById(answerId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid answerId=" + answerId));

            if (answer.getQuestion() == null || !Objects.equals(answer.getQuestion().getId(), qId)) {
                throw new IllegalArgumentException("answerId=" + answerId + " does not belong to questionId=" + qId);
            }

            intelligence += answer.getIntelligence();
            strength += answer.getStrength();
            defense += answer.getDefense();
            agility += answer.getAgility();
            magic += answer.getMagic();
        }

        int rawTotal = intelligence + strength + defense + agility + magic;
        if (rawTotal <= 0) {
            throw new IllegalStateException("Your answers produced zero stats.");
        }

        if (rawTotal > MAX_TOTAL_POINTS) {
            double scale = (double) MAX_TOTAL_POINTS / (double) rawTotal;
            intelligence = (int) Math.round(intelligence * scale);
            strength = (int) Math.round(strength * scale);
            defense = (int) Math.round(defense * scale);
            agility = (int) Math.round(agility * scale);
            magic = (int) Math.round(magic * scale);
            int scaledTotal = intelligence + strength + defense + agility + magic;
            if (scaledTotal > MAX_TOTAL_POINTS) {
                int overflow = scaledTotal - MAX_TOTAL_POINTS;
                defense = Math.max(0, defense - overflow);
            }
        }

        int totalPoints = intelligence + strength + defense + agility + magic;
        totalPoints = Math.min(totalPoints, MAX_TOTAL_POINTS);

        CharacterClassifier.Percentages p = classifier.classify(intelligence, strength, defense, agility, magic);

        Character character = new Character();
        character.setName(request.getCharacterName());
        character.setIntelligence(intelligence);
        character.setStrength(strength);
        character.setDefense(defense);
        character.setAgility(agility);
        character.setMagic(magic);
        character.setElfPercent(p.elf());
        character.setDwarfPercent(p.dwarf());
        character.setDragonPercent(p.dragon());
        character.setTotalPoints(totalPoints);
        character.setAvailablePoints(totalPoints);

        Character saved = characterRepository.save(character);
        return characterMapper.toDto(saved);
    }
}

