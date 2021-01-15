<#import "/spring.ftl" as spring/>

<!DOCTYPE HTML>
<html>
   <head>
      <meta charset="UTF-8" />
      <title>Оценка резюме</title>
      <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
      <link href="/css/font.css" rel="stylesheet">
      <link href="/css/materialdesignicons.min.css" rel="stylesheet">
      <link href="/css/vuetify.min.css" rel="stylesheet">
      <link href="/css/main.css" rel="stylesheet">
        <link href="/css/file-load.css" rel="stylesheet">
      <script src="/js/vue.js"></script>
      <script src="/js/vuetify.js"></script>
      <script src="/js/axios.min.js"></script>
      <script>
              document.addEventListener('DOMContentLoaded', function(){
               new Vue({
                    el: '#toolbar',
                    vuetify: new Vuetify(),
                  });
               new Vue({
                    el: '#profile-block',
                    vuetify: new Vuetify(),
                    data: {
                    formValid: true,
                        fullName: '',
                        email: '',
                        phone: '',
                         fullNameValidationRules: [
                                                                                                      value => !!value || "ФИО не может быть пустым"
                                                                                                   ],
                                                                                                    emailValidationRules: [
                                                                                                                              value => (!value ||  /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value.toLowerCase())) || "Неверный формат email"
                                                                                                                            ],
                        updating: false,
                        formValid: false,
                        loadingAvatar: false,
                        errAvatar: '',
                        currentAvatarPath: '',
                        userId: 0,
                        res: ''
                    },
                    mounted(){
                       this.fullName = document.getElementById('fullName').value;
                       this.email = document.getElementById('email').value;
                       this.phone = document.getElementById('phone').value;
                       this.currentAvatarPath = document.getElementById('currentAvatarPath').value;
                       this.userId =  document.getElementById('currentUserId').value;
                    },
                    methods:{
                      updateProfile(){
                          if(this.$refs.form.validate()){
                             this.updating = true;

                             let fullName = this.fullName;
                             let phone = this.phone;
                             let email = this.email;

                             this.res = '';

                             axios({
                                                                                                                                                                                                                                  url: '/profile/update',
                                                                                                                                                                                                                                  method:'post',
                                                                                                                                                                                                                                  data: {fullName: fullName, email: email, phone: phone}
                                                                                                                                                                                                                         })
                                                                                                                                                                                                                         .then(response=>{
                                                                                                                                                                                                                         this.updating = false;
                                                                                                                                                                                                                         this.res = 'Данные пользователя успешно обновлены';
                                                                                                                                                                                                                         })
                                                                                                                                                                                                                         .catch(error=>{
                                                                                                                                                                                                                                  this.updating = false;
                                                                                                                                                                                                                         });
                          }
                      },
                      loadAvatar(event){
                          if(event){
                          this.errAvatar = '';
                                                          let fileExtension = event.name.split('.');
                                                          fileExtension = fileExtension[fileExtension.length - 1].toLowerCase();

                                                          if(fileExtension!='jpg' && fileExtension != 'png'){
                                                             this.errAvatar = "Недопустимый формат файла. Допустимые форматы: jpg, png.";
                                                             return;
                                                          }

                                                          let formData = new FormData();
                                                                                              formData.append("avatar", event);

                                                                                              this.loadingAvatar = true;

                                                                                              axios({
                                                                                                                                                                                   url: '/users/loadAvatar/' + this.userId,
                                                                                                                                                                                   method:'post',
                                                                                                                                                                                   data: formData
                                                                                                                                                                          })
                                                                                                                                                                          .then(response=>{ console.log(response);
                                                                                                                                                                                 this.loadingAvatar = false;
                                                                                                                                                                                  this.currentAvatarPath = response.data.hrFullName;
                                                                                                                                                                                 }).catch(error=>{
                                                                                                                                                                                                                                                                                                            this.loadingAvatar = false;
                                                                                                                                                                                                                                                                                                            if(error.response){
                                                                                                                                                                                                                                                                                                              this.errAvatar = "Ошибка сервера";
                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                   });
                                                          }
                      }
                    }
                  });
             });
      </script>
   </head>
   <body>
      <div id="toolbar">
           <v-toolbar>
                         <#if isCanSeeManu == true>
                                       <v-menu bottom left>
                                        <template v-slot:activator="{ on }">
                                                     <v-btn
                                                       icon
                                                       v-on="on"
                                                     >
                                                       <v-icon>mdi-format-list-bulleted-square</v-icon>
                                                     </v-btn>
                                                   </template>
                                                               <v-list>
                                                               <#if isCanEditSettings == true>
                                                               <v-list-item>
                                                                                                                     <v-list-item-title><v-btn text href="/settings/all">Системные настройки</v-btn></v-list-item-title>
                                                                                                                   </v-list-item>
                                                                                                                   <v-list-item>
                                                                                                                                                                                                                                                                                                           <v-list-item-title><v-btn text href="/speciality/getall">Редактирование специальностей</v-btn></v-list-item-title>
                                                                                                                                                                                                                                                                                                         </v-list-item>
                                                                                                                                                                                                                                                                                                         <v-list-item>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       <v-list-item-title><v-btn text href="/users/all">Редактирование пользователей</v-btn></v-list-item-title>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     </v-list-item>
                                                                                                                   </#if>
                                                                            <v-list-item>
                                                                              <v-list-item-title><v-btn text href="/learn_example/all">Обучающие выборки</v-btn></v-list-item-title>
                                                                            </v-list-item>
                                                                          </v-list>
                                                              </v-menu>
                                                               </#if>
                                       <v-spacer></v-spacer>
                                       <v-toolbar-items>
                                        <#if isCanLoadFiles == true>
                                          <v-btn text href="/file/load">Оценка файла</v-btn>
                                          <v-btn text href="/file/all">Ранее загруженные файлы</v-btn>
                                        </#if>
                                        <v-avatar size="62">
                                                            <#if userAvatar == "">
                                                                                               <v-icon large
                                                                                                             color="blue-grey darken-2" title="${userFullName}">
                                                                                                      mdi-account-circle
                                                                                                    </v-icon>
                                                                                                     <#else>
                                                                                                      <img
                                                                                                             src="/avatar/get?file_path=${userAvatar}"
                                                                                                             alt="${userFullName}"
                                                                                                           >
                                                                                                                        </#if>
                                                                                            </v-avatar>
                                         <v-menu
                                                                               bottom
                                                                               left
                                                                             >
                                                                               <template v-slot:activator="{ on, attrs }">
                                                                                 <v-btn
                                                                                   icon
                                                                                   color="yellow"
                                                                                   v-bind="attrs"
                                                                                   v-on="on"
                                                                                 >
                                                                                   <v-icon>mdi-dots-vertical</v-icon>
                                                                                 </v-btn>
                                                                               </template>

                                                                               <v-list>
                                                                                 <v-list-item>
                                                                                   <v-list-item-title><v-btn text href="/profile">Редактировать профиль</v-btn></v-list-item-title>

                                                                                 </v-list-item>
                                                                                  <v-list-item>
                                                                                                                                                         <v-list-item-title>Выйти</v-list-item-title>
                                                                                                                                                          </v-list-item>
                                                                               </v-list>
                                                                             </v-menu>
                        </v-toolbar-items>
                      </v-toolbar>
       </div>
       <div id="profile-block">
              <v-app>
              <v-row>
               <v-col style="height: 91.8vh" cols="12" md="6">
                          <v-file-input
                                                                                                                                                                                                                                                                                                                            accept="image/*"
                                                                                                                                                                                                                                                                                                                            label="Загрузить новый аватар"
                                                                                                                                                                                                                                                                                                                            @change="loadAvatar($event)"
                                                                                                                                                                                                                                                                                                                          ></v-file-input>

                                                                                                                                                                           <v-progress-circular
                                                                                                                                                                                                                                                                                                                                                       indeterminate
                                                                                                                                                                                                                                                                                                                                                       color="primary"
                                                                                                                                                                                                                                                                                                                                                        v-if="loadingAvatar"
                                                                                                                                                                                                                                                                                                                                                     ></v-progress-circular>
               <h3 style="text-align: center; color: black">Аватар:</h3><div style="color:red" v-if="errAvatar!=''">{{errAvatar}}</div><v-img class="ml-12 mt-6" style="margin-left: 12vw !important"  max-height="1000" v-if="currentAvatarPath!=null && currentAvatarPath!=''"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             max-width="500" :src="'/avatar/get?file_path=' + currentAvatarPath">
               </v-col>
              <v-col cols="12" md="5" class="mr-3">
              <v-form ref="form" validation v-model="formValid">
                  <v-text-field
                                                                                                                                                                                                                                                                                                                                                                                                                                                                             label="ФИО"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                             outlined
                                                                                                                                                                                                                                                                                                                                                                                                                                                                             v-model="fullName"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                             :rules="fullNameValidationRules"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                           class="mt-1"></v-text-field>

              <v-text-field
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       label="Телефон"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       outlined
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       v-model="phone"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     class="mt-1"></v-text-field>
              <v-text-field
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      label="Email"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           outlined
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           v-model="email"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           :rules="emailValidationRules"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         class="mt-1"></v-text-field>
                <v-progress-circular
                                                                                                                                                                                                                                                                        indeterminate
                                                                                                                                                                                                                                                                        color="primary"
                                                                                                                                                                                                                                                                         v-if="updating"
                                                                                                                                                                                                                                                                      ></v-progress-circular>
              <div style="color:green" v-if="res!=''">{{res}}</div>
              <v-btn
                                                                                                                                                                                        color="primary"
                                                                                                                                                                                        style="float:right"
                                                                                                                                                                                        @click="updateProfile()"
                                                                                                                                                                                      >
                                                                                                                                                                                        Изменить
                                                                                                                                                                                      </v-btn>
             </v-form>
             </v-col>
             </v-row>
              </v-app>
              <input type="hidden" value="${userFullName}" id="fullName"/>
              <input type="hidden" value="${email}" id="email"/>
              <input type="hidden" value="${phone}" id="phone"/>
              <input type="hidden" value="${userAvatar}" id="currentAvatarPath"/>
              <input type="hidden" value="${userId}" id="currentUserId"/>
              </div>
       </body>
       </html>
