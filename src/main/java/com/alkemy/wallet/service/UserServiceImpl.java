package com.alkemy.wallet.service;
import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.dto.response.UserInfoResponseDto;
import com.alkemy.wallet.dto.request.UserUpdateRequestDto;
import com.alkemy.wallet.entity.Account;
import com.alkemy.wallet.entity.User;
import com.alkemy.wallet.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.alkemy.wallet.dto.response.PageableUserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{

    private final IUserRepository userRepository;  
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;

    public UserServiceImpl(IUserRepository userRepository, JwtServiceImpl jwtService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageableUserResponseDto getUsers(int page) {
        int pageToFind = page > 0 ? page-1 : 0;
        PageRequest pr = PageRequest.of(pageToFind,10);
        Page<User> userPage = userRepository.findAll(pr);
        long count = userPage.getTotalElements();
        int pages = userPage.getTotalPages();
        String prevPage = userPage.hasPrevious() ? "/api/v1/users?page="+(page-1) : null;
        String nextPage = userPage.hasNext() ? "/api/v1/users?page="+(page+1) : null;
        if(pages < page){
            return null;
        }
        List<User> users = userPage.getContent();
        List<UserDto> usersDto = users.stream().map((user) -> {
            List<Account> userAccounts = user.getAccounts();
            List<AccountDto> accountsDto = userAccounts.stream().map(account -> {
                return new AccountDto(
                        user.getEmail(),
                        account.getId(),
                        account.getCurrency().name(),
                        account.getBalance(),
                        account.getTransactionLimit(),
                        account.getCreationDate()
                );
            }).toList();
            return new UserDto(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole().getName().name(),
                    accountsDto
            );
        }).toList();
        return new PageableUserResponseDto(
                count,
                pages,
                prevPage,
                nextPage,
                usersDto
        );
    }

    @Override
    public UserInfoResponseDto getUserById(Long id, String token) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            String userEmail = jwtService.extractUsername(token.substring(7));
            if(Objects.equals(user.getEmail(), userEmail)){
                return new UserInfoResponseDto(
                        userEmail,
                        user.getFirstName(),
                        user.getLastName(),
                        user.getCreationDate(),
                        user.getUpdateDate()
                );
            }
        }
        return null;
    }

    @Override
    public UserInfoResponseDto deleteUserById(Long id,String token) {
        Optional<User> userOptional =userRepository.findById(id);
        if(userOptional.isPresent()){
            User user=userOptional.get();
            String userEmail=jwtService.extractUsername(token.substring(7));
            if(Objects.equals(user.getEmail(), userEmail)){
                user.setSoftDelete(Boolean.TRUE);
                userRepository.save(user);
                return new UserInfoResponseDto(
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getCreationDate(),
                        user.getUpdateDate()
                );
            }

        }
        return null;
    }

    @Override
    public UserInfoResponseDto updateUser(Long id, UserUpdateRequestDto userRequest, String token) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            String userEmail = jwtService.extractUsername(token.substring(7));
            if(Objects.equals(userEmail, user.getEmail())){
                boolean isUpdated = false;
                if(userRequest.getFirstName() != null && !userRequest.getFirstName().isBlank()){
                    user.setFirstName(userRequest.getFirstName());
                    isUpdated = true;
                }
                if(userRequest.getLastName() != null && !userRequest.getLastName().isBlank()){
                    user.setLastName(userRequest.getLastName());
                    isUpdated = true;
                }
                if(userRequest.getPassword() != null && !userRequest.getPassword().isBlank()){
                    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                    isUpdated = true;
                }
                if(isUpdated){
                    userRepository.save(user);
                    return new UserInfoResponseDto(
                            user.getEmail(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getCreationDate(),
                            user.getUpdateDate()
                    );
                }
            }
        }
        return null;
    }
}
