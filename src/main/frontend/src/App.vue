<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo">
      System do zapisów na zajęcia
    </h1>
     <div :class="isErrorMessage ? 'red' : 'green'" v-if="message"> {{message}} </div>
    <div v-if="authenticatedUsername.login !== '' && !registering">
      <h2>Witaj {{ authenticatedUsername.login }}!
        <a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
      </h2>
      <meetings-page :username="authenticatedUsername.login"></meetings-page>
    </div>
    <div v-else-if="!authenticatedUsername.login && !registering">
      <login-form @login="login($event)" @registeringStarted="registering = $event"></login-form>
    </div>
    <div v-else-if="!authenticatedUsername.login && registering">
        <register-form @registered="register($event)"></register-form>
    </div>
  </div>
</template>

<script>
    import "milligram";
    import RegisterForm from "./meetings/RegisterForm";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";

    export default {
        components: {LoginForm, MeetingsPage, RegisterForm},
        data() {
            return {
                authenticatedUsername: {
                    login: '',
                    password: ''
                },
                registering: false
            };
        },
        methods: {
            login(user) {
                this.authenticatedUsername.login = user.login;
            },
            logout() {
                this.authenticatedUsername.login = '';
            },
            register(user) {
                 this.$http.post('participants', user)
                     .then(response => {
                         this.message = "Udało się!";
                         this.isErrorMessage = false;
                     })
                     .catch(response => {
                         this.message = "Nie udało się!";
                         this.isErrorMessage = true;
                     });
                this.registering = false;
                this.authenticatedUsername.login = user.login;
                this.authenticatedUsername.password = user.password;
            }
        }
    };
</script>

<style>
  #app {
    max-width: 1000px;
    margin: 0 auto;
  }

  .red {background: red}

  .green {background: green}

  .logo {
    vertical-align: middle;
  }
</style>

