package com.phlow.server.web.followings;

import com.phlow.server.domain.model.users.UserModel;
import com.phlow.server.domain.model.users.UserModelMapper;
import com.phlow.server.service.followings.FollowingsService;
import com.phlow.server.web.users.dto.UserDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "Followings")
@RestController
@RequestMapping(
        path = "${phlow.api.prefix:/api}" + "${phlow.api.versions.v1:/v1}" + "${phlow.api.mappings.followings:/followings}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class FollowingController {

    private FollowingsService followingsService;
    private UserModelMapper userModelMapper;

    @Autowired
    public FollowingController(FollowingsService followingsService,
                               UserModelMapper userModelMapper){
        this.followingsService = followingsService;
        this.userModelMapper = userModelMapper;
    }

    @GetMapping("/{userId}/followers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDto>> getFollowers(@PathVariable final String userId) {
        return ResponseEntity.ok(this.userModelMapper.modelsToDtos(this.followingsService.getFollowers(userId)));
    }

    @GetMapping("/{userId}/followings")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDto>> getFollowings(@PathVariable final String userId) {
        return ResponseEntity.ok(this.userModelMapper.modelsToDtos(this.followingsService.getFollowings(userId)));
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity follow(@PathVariable final String userId,
                                 @ApiIgnore @AuthenticationPrincipal UserModel follower) {
        this.followingsService.follow(userId, follower.getId().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity unfollow(@PathVariable final String userId,
                                   @ApiIgnore @AuthenticationPrincipal UserModel follower) {
        this.followingsService.unfollow(userId, follower.getId().toString());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
