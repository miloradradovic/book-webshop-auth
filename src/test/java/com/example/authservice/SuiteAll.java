package com.example.authservice;

import com.example.authservice.api.AuthControllerUnitTests;
import com.example.authservice.api.UserControllerUnitTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthControllerUnitTests.class, UserControllerUnitTests.class})
public class SuiteAll {
}
