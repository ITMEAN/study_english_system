package org.minh.flashcardservice.service.flashcard;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.minh.flashcardservice.dto.UserDTO;
import org.minh.flashcardservice.dto.response.ListFlashCardResponse;
import org.minh.flashcardservice.entity.FlashCard;
import org.minh.flashcardservice.entity.FlashCardUser;
import org.minh.flashcardservice.entity.Vocabulary;
import org.minh.flashcardservice.exception.BadCredentialsException;
import org.minh.flashcardservice.exception.DataNotFoundException;
import org.minh.flashcardservice.exception.S3UploadException;
import org.minh.flashcardservice.httpClient.IdentityClient;
import org.minh.flashcardservice.dto.request.AddVocabularyRequestDTO;
import org.minh.flashcardservice.dto.request.CreateFlashCardRequestDTO;
import org.minh.flashcardservice.dto.request.RemoveVocabularyRequestDTO;
import org.minh.flashcardservice.repositories.FlashCardRepository;
import org.minh.flashcardservice.repositories.FlashCardUserRepository;
import org.minh.flashcardservice.service.FlashCardUser.FlashCardUserService;
import org.minh.flashcardservice.service.jwt.JwtUtil;
import org.minh.flashcardservice.service.s3upload.AmazonS3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashCardServiceImpl implements FlashCardService {
    @Autowired
    private final IdentityClient identityClient;
    @Autowired
    private final FlashCardRepository flashCardRepository;
    @Autowired
    private final AmazonS3UploadService amazonS3UploadService;
    @Autowired
    private final FlashCardUserService flashCardUserService;
    @Autowired
    private final JwtUtil jwtUtil;


    @Override
    public FlashCard addFlashCard(CreateFlashCardRequestDTO request, String authorization) throws BadCredentialsException {
        String token = authorization.replace("Bearer ", "");
        String email = jwtUtil.getSubject(token);

        if (!email.equals(request.getCreatedBy())) {
            throw new BadCredentialsException("Token is invalid");
        }
        UserDTO userDTO = identityClient.getUserByEmail(request.getCreatedBy(), authorization);
        if (userDTO == null) {
            throw new DataNotFoundException("User is not exist");
        }

        if (!identityClient.isExistUser(request.getCreatedBy(), "Bearer " + token)) {
            throw new DataNotFoundException("User is not exist");
        }
        FlashCard flashCard = FlashCard.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(request.getCreatedBy())
                .createByAdmin(userDTO.getRole().equals("ADMIN"))
                .createName(userDTO.getFullName()).build();
        return flashCardRepository.save(flashCard);
    }

    @Override
    public FlashCard AddVocabulary(AddVocabularyRequestDTO request,String authorization) throws S3UploadException, BadCredentialsException {
        System.out.println(request.getIdFlashCard());
        System.out.println("request.getWord() = " + request.getWord());

        String token = authorization.replace("Bearer ", "");
        String email = jwtUtil.getSubject(token);
        FlashCard flashCard = flashCardRepository.findById(request.getIdFlashCard()).orElseThrow(() -> new DataNotFoundException("Flash card not found"));
        if(!flashCard.getCreatedBy().equals(email)){
            throw new BadCredentialsException("you are not the owner of this flash card");
        }
        Vocabulary vocabulary = Vocabulary.builder().word(request.getWord()).meaning(request.getMeaning()).example(request.getExample()).spellings(request.getSpellings()).id(new ObjectId().toString()).build();
        if (request.getImg() != null) {
            String img = amazonS3UploadService.uploadFile(request.getImg());
            vocabulary.setImg(img);
        }
        if (request.getAudio() != null) {
            String audio = amazonS3UploadService.uploadFile(request.getAudio());
            vocabulary.setAudio(audio);
        }
        flashCard.getVocabularies().add(vocabulary);
        return flashCardRepository.save(flashCard);
    }

    @Override
    public FlashCard removeVocabulary(RemoveVocabularyRequestDTO request,String authorization) throws BadCredentialsException {
        String token = authorization.replace("Bearer ", "");
        String email= jwtUtil.getSubject(token);
        System.out.println("request.getIdFlashCard() = " + request.getIdFlashCard());
        FlashCard flashCard = flashCardRepository.findById(request.getIdFlashCard()).orElseThrow(() -> new DataNotFoundException("Flash card not found"));
        if(!flashCard.getCreatedBy().equals(email)){
            throw new BadCredentialsException("you are not the owner of this flash card");
        }
        request.getIdVocabularies().forEach(id -> {
            flashCard.getVocabularies().removeIf(vocabulary -> vocabulary.getId().equals(id));
        });
        return flashCardRepository.save(flashCard);
    }

    @Override
    public FlashCard findFlashCardById(String id) throws BadCredentialsException {
        System.out.println("id = " + id);
        return flashCardRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Flash card not found"));
    }

    @Override
    public ListFlashCardResponse getFlashCardsByEmail(int page, int size, String email, String authorization) throws BadCredentialsException {
      try{
          String token = authorization.replace("Bearer ", "");


          UserDTO userDTO = identityClient.getUserByEmail(email, "Bearer " + token);
          System.out.println("userDTO = " + userDTO);
          if(userDTO == null){
              throw new BadCredentialsException("User is not exist");
          }
          if(!identityClient.getUserByEmail(email, "Bearer " + token).getRole().equals("ADMIN")){
              throw new BadCredentialsException("You are not the owner of this flash card");
          }


          Page<FlashCard>  flashCards = flashCardRepository.findAllByCreatedBy(email, PageRequest.of(page-1, size));
          return ListFlashCardResponse.builder().flashCards(flashCards.getContent()).total(flashCards.getTotalPages()).build();
      }catch (Exception e){
          System.err.println(e.getMessage());
          throw new BadCredentialsException("Token is invalid");
      }
    }

    @Override
    public ListFlashCardResponse getFlashCardsPublic(int page, int size) throws BadCredentialsException {
        Page<FlashCard>  flashCards = flashCardRepository.findAll(PageRequest.of(page-1, size));
        return ListFlashCardResponse.builder().flashCards(flashCards.getContent()).total(flashCards.getTotalPages()).build();
    }


    @Override
    public void removeFlashCard(String idFlashCard) {
       if(flashCardRepository.existsById(idFlashCard)){
              flashCardRepository.deleteById(idFlashCard);
       }else{
           throw new DataNotFoundException("Flash card not found");
       }
    }


    @Override
    public List<FlashCard> findFlashCardByIds(List<String> ids) {
        return ids.stream().map(id -> flashCardRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Flash card not found"))).toList();
    }

    @Override
    public List<FlashCard> findFlashCardLearned(String email) {
        List<FlashCardUser> flashCardUsers = flashCardUserService.findAllByEmail(email);
        return flashCardUsers.stream().map(flashCardUser -> flashCardRepository.findById(flashCardUser.getIdFlashCardSet()).orElseThrow(() -> new DataNotFoundException("Flash card not found"))).toList();
    }
}
