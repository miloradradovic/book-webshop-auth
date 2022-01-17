package com.example.authservice;

import com.example.authservice.api.AuthControllerUnitTests;
import com.example.authservice.api.UserControllerUnitTests;
import com.example.authservice.service.AuthServiceUnitTests;
import com.example.authservice.service.UserServiceUnitTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthControllerUnitTests.class, UserControllerUnitTests.class, AuthServiceUnitTests.class, UserServiceUnitTests.class})
public class SuiteAll {
}
