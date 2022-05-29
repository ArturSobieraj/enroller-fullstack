<template>
  <form @submit.prevent="enter()">
    <label>Login</label>
    <input type="text" v-model="user.login">
    <label>Hasło</label>
    <input type="password" v-model="user.password">
    <button type="submit">{{ labelOfTheButton }}</button>
    <button class="register" @click="startRegistering"> Zarejestruj </button>
  </form>
</template>

<script>
    export default {
        props: ["buttonLabel"],
        data() {
            return {
                user: {}
            };
        },
        methods: {
            enter() {
                this.$emit("login", this.user);
            },
            startRegistering() {
                this.$emit("registeringStarted", true);
            }
        },
        computed: {
            labelOfTheButton() {
                return this.buttonLabel || 'Zaloguj się';
            }
        }
    };
</script>
<style>
    .register {
        margin: 2px;
    }
</style>
