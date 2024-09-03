package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dtos.LoginRequestDto;
import org.example.dtos.LoginResponseDto;
import org.example.entities.User;
import org.example.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService{
    private final PasswordHashing passwordHashing;
    private final UsersRepository usersRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        var user = usersRepository.findByUsername(loginRequestDto.getUsername());
        if (user.isPresent()){
           if (passwordHashing.checkPasswords(loginRequestDto.getPassword(), user.get().getPassword())){
               return new LoginResponseDto(true, user.get().getUuid());
           }
        }
        return new LoginResponseDto(false, null);
    }

    @Override
    public LoginResponseDto register(LoginRequestDto loginRequestDto) {
        var passwordHash = passwordHashing.generatePasswordHash(loginRequestDto.getPassword());
        UUID userId = UUID.randomUUID();
        usersRepository.save(new User(userId, loginRequestDto.getUsername(), passwordHash));
        return new LoginResponseDto(true, userId);
    }
}
