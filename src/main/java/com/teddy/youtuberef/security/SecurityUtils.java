package com.teddy.youtuberef.security;

import com.teddy.youtuberef.web.rest.error.MessageCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Muc đích của lớp SecurityUtils là cung cấp các phương thức tiện ích để truy cập thông tin về người dùng hiện tại và quyền của họ trong ứng dụng Spring Security.
 */
public final class SecurityUtils {
    private SecurityUtils() {}

    public static Optional<String> getCurrentUserLogin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    /**
     * Hàm này sẽ trích xuất thông tin về principal (thường là username) từ đối tượng Authentication.
     * Nó kiểm tra nếu Authentication không null, sau đó kiểm tra kiểu của principal.
     * Nếu principal là một instance của UserDetails, nó sẽ lấy username từ đó.
     * Nếu principal là một String, nó sẽ trả về trực tiếp. Nếu không có thông tin nào phù hợp, nó sẽ trả về null.
     * @param authentication
     * @return The login current user
     */
    private static String extractPrincipal(Authentication authentication){
        if(authentication == null){
            return null;
        }else if(authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if(authentication.getPrincipal() instanceof String){
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * Mục đích hàm này là để lấy JWT (JSON Web Token) của người dùng hiện tại từ đối tượng Authentication trong SecurityContext.
     * Nó kiểm tra nếu Authentication không null và nếu credentials của Authentication là một String (thường là JWT), nó sẽ trả về JWT đó. Nếu không có JWT nào phù hợp,
     * nó sẽ trả về một Optional rỗng.
     * @return The JWT of current user
     */
    public static Optional<String> getCurrentUserJWT(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional
                .ofNullable(securityContext.getAuthentication())
                .filter((authentication -> authentication.getCredentials() instanceof String))
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     * Hàm này kiểm tra xem người dùng hiện tại đã được xác thực hay chưa bằng cách lấy đối tượng Authentication từ SecurityContext.
     * Nó kiểm tra nếu Authentication không null và nếu người dùng không có quyền "ANONYMOUS" (thường được sử dụng để biểu thị người dùng chưa đăng nhập).
     * Nếu cả hai điều kiện này đều đúng, hàm sẽ trả về true, ngược lại sẽ trả về false.
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    }

    /**
     * Lấy danh sách các quyền (authorities) của người dùng hiện tại từ đối tượng Authentication.
     * Hàm này sẽ trả về một Stream chứa tên của các quyền mà người dùng hiện tại có.
     * Nó sử dụng phương thức getAuthorities() của Authentication để lấy danh sách các GrantedAuthority
     * và sau đó chuyển đổi chúng thành một Stream các String bằng cách gọi getAuthority() trên mỗi GrantedAuthority.
     * @param authentication
     * @return
     * example: Nếu người dùng có hai quyền "ROLE_USER" và "ROLE_ADMIN", hàm này sẽ trả về một Stream chứa "ROLE_USER" và "ROLE_ADMIN".
     */
    public static Stream<String> getAuthorities(Authentication authentication){
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    }

    /**
     * Hàm này kiểm tra xem người dùng hiện tại có bất kỳ quyền nào trong số các quyền được cung cấp hay không.
     * Nó lấy đối tượng Authentication từ SecurityContext và sau đó sử dụng phương thức getAuthorities
     * @param authorities
     * @return
     * example: Nếu người dùng hiện tại có quyền "ROLE_USER" và gọi hasCurrentUserAnyOfAuthorities("ROLE_ADMIN", "ROLE_USER"),
     * hàm sẽ trả về true vì người dùng có quyền "ROLE_USER". Nếu gọi hasCurrentUserAnyOfAuthorities("ROLE_ADMIN"), hàm sẽ trả về false vì người dùng không có quyền "ROLE_ADMIN".
     */
    public static boolean hasCurrentUserAnyOfAuthorities(String ... authorities){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (
                authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
                );
    }

    /**
     * Tương tự như hàm hasCurrentUserAnyOfAuthorities, nhưng nó kiểm tra xem người dùng hiện tại có không có bất kỳ quyền nào trong số các quyền được cung cấp hay không.
     * @param authorities
     * @return
     * example: Nếu người dùng hiện tại có quyền "ROLE_USER" và gọi hasCurrentUserNoneOfAuthorities("ROLE_ADMIN", "ROLE_GUEST"),
     * hàm sẽ trả về true vì người dùng không có quyền "ROLE_ADMIN" và "ROLE_GUEST".
     * Nếu gọi hasCurrentUserNoneOfAuthorities("ROLE_USER"), hàm sẽ trả về false vì người dùng có quyền "ROLE_USER".
     */

    public static boolean hasCurrentUserNoneOfAuthorities(String ... authorities){
        return !hasCurrentUserAnyOfAuthorities(authorities);
    }

    /**
     * Hàm này kiểm tra xem người dùng hiện tại có một quyền cụ thể hay không bằng cách gọi hàm hasCurrentUserAnyOfAuthorities với một quyền duy nhất.
     * @param authority
     * @return
     * example: Nếu người dùng hiện tại có quyền "ROLE_USER" và gọi hasCurrentUserThisAuthority("ROLE_USER"), hàm sẽ trả về true. Nếu gọi hasCurrentUserThisAuthority("ROLE_ADMIN"), hàm sẽ trả về false.
     */
    public static boolean hasCurrentUserThisAuthority(String authority){
        return hasCurrentUserAnyOfAuthorities(authority);
    }

    /**
     * Hàm này được sử dụng để trả về một phản hồi HTTP với mã lỗi và thông điệp lỗi khi người dùng không có đủ quyền truy cập hoặc khi xác thực thất bại.
     * @param response
     * @param status
     * @param message
     * @throws IOException
     * Response structure:
     * {
     *   "code": "401",
     *   "message": "Unauthorized"
     * }
     */
    public static void responseFailCredential(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        new ObjectMapper()
                .writeValue(response.getOutputStream(), new MessageCode(String.valueOf(status.value()), message));
        response.flushBuffer();
    }
}
