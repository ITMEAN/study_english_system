package org.minh.flashcardservice.service.FlashCardUser;

import lombok.RequiredArgsConstructor;
import org.minh.flashcardservice.dto.UserDTO;
import org.minh.flashcardservice.dto.request.AddFlashCardUser;
import org.minh.flashcardservice.entity.FlashCardUser;
import org.minh.flashcardservice.exception.DataNotFoundException;
import org.minh.flashcardservice.httpClient.IdentityClient;
import org.minh.flashcardservice.repositories.FlashCardUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FlashCardUserServiceImpl implements FlashCardUserService {
    @Autowired
    private final FlashCardUserRepository flashCardUserRepository;
    @Autowired
    private final IdentityClient identityClient;
    @Override
    public FlashCardUser findFlashCardUserById(String id) {
        return flashCardUserRepository.findByIdFlashCardSet(id).orElseThrow(() -> new DataNotFoundException("FlashCardUser not found"));

    }

    @Override
    public FlashCardUser addFlashCardUser(AddFlashCardUser request,String token) {
        UserDTO userDTO = identityClient.getUserByEmail(request.getEmail(), token);
        if (userDTO == null) {
            throw new DataNotFoundException("User is not exist");
        }
        FlashCardUser flashCardUser = FlashCardUser.builder()
                .email(request.getEmail())
                .idFlashCardSet(request.getIdFlashCardSet())
                .build();
        return flashCardUserRepository.save(flashCardUser);
    }

    @Override
    public List<FlashCardUser> findAllByEmail(String email) {
        return flashCardUserRepository.findAllByEmail(email);
    }

    @Override
    public FlashCardUser findByEmailAndIdFlashCardSet(String email, String idFlashCardSet) {
        return flashCardUserRepository.findByEmailAndIdFlashCardSet(email, idFlashCardSet).orElseThrow(() -> new DataNotFoundException("FlashCardUser not found"));
    }


}
