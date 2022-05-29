<template>
    <div>
     <h1>
      Zarejestruj się
    </h1>
    <form @submit.prevent="register()">
        <label>Login</label>
        <input type="text" v-model="newLogin">
        <label>Hasło</label>
        <input type="password" v-model="newPassword">
        <label>Powtórz hasło</label>
        <input type="password" v-model="newConfirmPassword">
        <button type="submit">Utwórz konto</button>
        <label class="passwordError" v-if="this.passwordError">Podane hasła nie są takie same</label>
      </form>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                newLogin: '',
                newPassword: '',
                newConfirmPassword: '',
                passwordError: false
            };
        },
        methods: {
            register() {
                this.passwordError = this.newPassword !== this.newConfirmPassword;
                if (!this.passwordError) {
                    this.$emit("registered", {
                        login: this.newLogin,
                        password: this.newPassword
                    });
                    this.newLogin = '';
                    this.newPassword = '';
                    this.newConfirmPassword = '';
                }
            }
        }
    }
</script>
<style>
    .passwordError {
        color: red;
    }
</style>