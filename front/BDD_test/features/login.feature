Feature: Login Feature File
  test use the root username and password to login

  Scenario: login ProgEdu
    Given navigate to login page
    When user logged in using username as "root" and password as "rootrootrootroot" 
    Then I will see Hi,root