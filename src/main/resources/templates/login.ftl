 <html>
 <head>
    <meta charset="UTF-8" />
         <title>Оценка резюме</title>
         <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
         <link href="/css/font.css" rel="stylesheet">
         <link href="/css/materialdesignicons.min.css" rel="stylesheet">
         <link href="/css/vuetify.min.css" rel="stylesheet">
         <link href="/css/main.css" rel="stylesheet">
         <link href="/css/login-form.css" rel="stylesheet">
         <script src="/js/vue.js"></script>
         <script src="/js/vuetify.js"></script>
         <script src="/js/axios.min.js"></script>
          <script>
                       document.addEventListener('DOMContentLoaded', function(){
                        new Vue({
                             el: '#login-form',
                             vuetify: new Vuetify(),
                             data: {
                                login: '',
                                password: '',
                                validateRules: [
                                  value => !!value || "Логин и пароль не могут быть пустыми"
                                ],
                                valid: false
                             },
                             methods:{
                               submitForm(){
                                if (this.$refs.form.validate()) {
                                   this.$refs.form.$el.submit();
                                }
                               }
                             }
                           });
                           });
                           </script>
 </head>
 <body>
  <div id="login-form">
 <div class="loginWrapper">
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card class="elevation-12" align="center" justify="center">
          <v-toolbar class="auth-tool-bar" color="indigo" dark flat>
            <v-toolbar-title>Авторизация</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-form ref="form" validation v-model="valid"  action="login" method='POST' @keyup.native.enter="submitForm">
              <v-text-field
                label="Введите логин"
                name="username"
                type="text"
                v-model="login"
                :rules="validateRules"
              ></v-text-field>
              <v-text-field
                id="password"
                label="Введите пароль"
                name="password"
                type="password"
                v-model="password"
                :rules="validateRules"
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
<#if error>
            <v-row style="color:red">
            Ошибка: неверный логин или пароль
            </v-row> <br/></#if>
            <v-btn
              class = "login-button"
              color="primary"
              :disabled="!valid"
              @click.prevent = "submitForm"
            >Войти</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </div>
  </div>
  </body>
  </html>