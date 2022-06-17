<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo">
      System do zapisów na zajęcia
    </h1>
     <div :class="isErrorMessage ? 'red' : 'green'" v-if="message"> {{message}} </div>
    <div v-if="authenticatedUserName !== '' && !registering">
      <h2>Witaj {{ authenticatedUserName }}!
        <a @click="logout()" class="float-right button-outline button">Wyloguj</a>
      </h2>
      <meetings-page :username="authenticatedUserName"></meetings-page>
    </div>
    <div v-else-if="!authenticatedUserName && !registering">
      <login-form @login="login($event)" @registeringStarted="registering = $event"></login-form>
    </div>
    <div v-else-if="!authenticatedUserName && registering">
        <register-form @registered="register($event)"></register-form>
    </div>
  </div>
</template>

<script>
    import "milligram";
    import RegisterForm from "./meetings/RegisterForm";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";
    import Vue from "vue";

    export default {
        components: {LoginForm, MeetingsPage, RegisterForm},
        data() {
            return {
                isErrorMessage: false,
                message: '',
                authenticatedUserName: "",
                registering: false
            };
        },
        methods: {
            login(user) {
                this.message = "";
                this.$http.post("tokens", user)
                    .then(response => {
                      const token = response.body.token;
                      this.storeAuth(user.login, token);
                    })
                    .catch(() => this.handleFailure("Błędny użytkownik lub hasło"));
            },
            logout() {
                this.authenticatedUserName = '';
                delete Vue.http.headers.common.Authorization;
                localStorage.clear();
            },
            register(user) {
              this.message = "";
                 this.$http.post('participants', user)
                     .then(() => {
                         this.success("Konto zostało założone");
                     })
                     .catch(response => {
                         this.handleFailure("Błąd podczas zakładania konta: " + response.status);
                     });
                this.registering = false;
            },
            storeAuth(userName, token) {
              this.authenticatedUserName = userName;
              Vue.http.headers.common.Authorization = "Bearer " + token;
              localStorage.setItem("userName", userName);
              localStorage.setItem("token", token);
            },
            success(message) {
              this.message = message;
              this.isErrorMessage = false;
            },
            handleFailure(message) {
              this.message = message;
              this.isErrorMessage = true;
            }
        },
      mounted() {
          const userName = localStorage.getItem("userName");
          const token = localStorage.getItem("token");
          if (userName && token) {
            this.storeAuth(userName, token);
            this.$http.get("participants/${userName}").catch(() => this.logout());
          }
      }
    };
</script>

<style>
  #app {
    max-width: 1000px;
    margin: 0 auto;
  }

  .red {
    border: black solid;
    background: red;
    margin-bottom: 5px;
    color: white;
  }

  .green {
    border: black solid;
    background: green;
    margin-bottom: 5px;
    color: white;
  }

  .logo {
    vertical-align: middle;
  }
</style>

