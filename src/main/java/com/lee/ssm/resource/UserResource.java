package com.lee.ssm.resource;


import com.lee.ssm.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("user")
@Api(value = "user resource")
public class UserResource {

    @ApiOperation("get user")
    @GetMapping()
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok(new User("Tom", 20));
    }




}
