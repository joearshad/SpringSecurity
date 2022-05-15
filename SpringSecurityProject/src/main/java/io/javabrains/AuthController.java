package io.javabrains;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@PostMapping("/subs")
	private ResponseEntity<?> subscribeClient(@RequestBody AuthenticationRequest authenticationRequest) {
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		UserModel userModel = new UserModel();
		userModel.setUsername(username);
		userModel.setPassword(password);
		try {
			userRepository.save(userModel);
		} catch(Exception e) {
			return ResponseEntity.ok(new AuthenticationResponse("Error during client Subscription " + username));
		}
		return ResponseEntity.ok(new AuthenticationResponse("Successful Subscription for client " + username));
	}
	
	@PostMapping("/auth")
	private ResponseEntity<?> authenticateClient(@RequestBody AuthenticationRequest authenticationRequest) {
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch(BadCredentialsException e) {
			return ResponseEntity.ok(new AuthenticationResponse("Incorrect password for " + username));
		} catch(InternalAuthenticationServiceException e) {
			return ResponseEntity.ok(new AuthenticationResponse("your Email " + username + " is not available"));
		} catch(Exception e) {
			return ResponseEntity.ok(new AuthenticationResponse("Error during client Authentication " + username));
		}
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		return ResponseEntity.ok(new AuthenticationResponse("Successful Authentication for client " + username));
	}

}
