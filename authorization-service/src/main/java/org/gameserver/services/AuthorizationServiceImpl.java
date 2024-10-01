package org.gameserver.services;

import lombok.RequiredArgsConstructor;
import org.gameserver.dtos.LoginRequestDto;
import org.gameserver.dtos.LoginResponseDto;
import org.gameserver.entities.User;
import org.gameserver.errors.BadDataException;
import org.gameserver.errors.NotFoundException;
import org.gameserver.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService{
    private final PasswordHashing passwordHashing;
    private final UsersRepository usersRepository;
    private final Logger logger = Logger.getLogger(AuthorizationServiceImpl.class.getName());

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        var user = usersRepository.findByUsername(loginRequestDto.getUsername());
        if (user.isPresent()){
           if (passwordHashing.checkPasswords(loginRequestDto.getPassword(), user.get().getPassword())){
               return new LoginResponseDto(true, user.get().getUuid());
           } else {
               throw new BadDataException("Invalid password");
           }
        }
        throw new NotFoundException("User not found");
    }

    @Override
    public LoginResponseDto register(LoginRequestDto loginRequestDto) {
        if (loginRequestDto.getUsername().isEmpty()){
            throw new BadDataException("Username is empty");
        }
        if (loginRequestDto.getPassword().isEmpty()){
            throw new BadDataException("Password is empty");
        }

        logger.warning("Registering user with username: " + loginRequestDto.getUsername() + " and password: " + loginRequestDto.getPassword());
        var passwordHash = passwordHashing.generatePasswordHash(loginRequestDto.getPassword());
        UUID userId = UUID.randomUUID();

        try {
            usersRepository.save(new User(userId, loginRequestDto.getUsername(), passwordHash));
        } catch (Exception e){
            throw new BadDataException("User already exists");
        }

        return new LoginResponseDto(true, userId);
    }

    @Override
    public boolean checkToken(String token){
        return usersRepository.findByUuid(UUID.fromString(token)).isPresent();
    }
}
