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
                    el: '#file-load-grid',
                    vuetify: new Vuetify(),
                    data: {
                        files:[],
                        coastedFiles: [],
                        filesEmpty: true,
                        fileImgSrc: '/img/word-picture.png',
                        alignment: 'flex-end',
                        dialog:false,
                        loadFile: false
                    },
                    computed: {
                        dialogVisible: {
                          get: function () {
                            return this.dialog;
                          },
                          set: function (value) {
                                 if (!value) {
                                   this.$emit('close', null)
                                 }
                          }
                      }
                    },
                    methods:{
                            addFile(e) {
                                  let droppedFiles = e.dataTransfer.files;
                                  if(droppedFiles.length == 0) return true;

                                  let fileExtension = droppedFiles[0].name.split(".").pop();

                                  if(fileExtension != "docx" && fileExtension != "pdf" && fileExtension != "zip"){
                                       this.dialog = true;
                                       return true;
                                  }

                                  this.files = [];
                                  this.coastedFiles = [];

                                  ([...droppedFiles]).forEach(f => {
                                    this.files.push(f);
                                  });

                                  if(this.files.length != 0){
                                     this.filesEmpty = false;
                                  }

                                  this.loadFile = true;

                                   let formData = new FormData();
                                        this.files.forEach((f,x) => {
                                          formData.append('file', f);
                                        });

                                        fetch('/file/coast', {
                                          method:'POST',
                                          body: formData
                                        })
                                        .then(res => res.json())
                                        .then(res => {
                                           this.loadFile = false;
                                           let self = this;

                                           res.forEach(file => {
                                              self.coastedFiles.push(file);
                                           });
                                        })
                                        .catch(e => {
                                          this.loadFile = false;
                                          console.error(JSON.stringify(e.message));
                                        });
                             },
                             loadFilesFromInput(e) {
                                  let droppedFiles = e.target.files;
                                  if(droppedFiles.length == 0) return true;

                                  let fileExtension = droppedFiles[0].name.split(".").pop();

                                  if(fileExtension != "docx" && fileExtension != "pdf" && fileExtension != "zip"){
                                      this.dialog = true;
                                      return true;
                                  }

                                  this.files = [];
                                  this.coastedFiles = [];

                                 ([...droppedFiles]).forEach(f => {
                                                  this.files.push(f);
                                 });

                                 if(this.files.length != 0){
                                        this.filesEmpty = false;
                                }

                                this.loadFile = true;

                                 let formData = new FormData();
                                                                        this.files.forEach((f,x) => {
                                                                          formData.append('file', f);
                                                                        });

                                                                        fetch('/file/coast', {
                                                                          method:'POST',
                                                                          body: formData
                                                                        })
                                                                        .then(res => res.json())
                                                                        .then(res => {
                                                                           this.loadFile = false;
                                                                           console.log(res);
                                                                           let self = this;

                                                                           res.forEach(file => {
                                                                                  self.coastedFiles.push(file);
                                                                           });
                                                                        })
                                                                        .catch(e => {
                                                                          this.loadFile = false;
                                                                          console.error(JSON.stringify(e.message));
                                                                        });
                            },
                            showLoadFileDialog(e){
                              document.getElementById("file").click();
                            },
                            formatNumber(value) {
                                    let val = (value/1).toFixed(2).replace('.', ',');
                                    return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
                            },
                            getProgressValue(value) {
                                              return value * 100;
                            },
                             getProgressColor(value) {
                                        if(value > 0.65){
                                          return "green";
                                        }

                                        if(value > 0.4){
                                                   return "lime";
                                         }

                                         return "red";
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

      <div id="file-load-grid">
       <v-app>
                <v-container class="grey lighten-5">
                    <v-row no-gutters>
                      <v-col
                        cols="12"
                        sm="6"
                        class="file-load-col"
                        v-cloak @drop.prevent="addFile" @dragover.prevent @click="showLoadFileDialog"
                         style="min-height:91.8vh"
                      >
                          <v-card
                           class="mx-auto file-card"
                          v-for="file in files">
                             <v-img
                             :src="fileImgSrc"
                             class="white--text align-end"
                             >
                             </v-img>
                              <v-card-subtitle class="pb-0 file-card-title">{{file.name}}</v-card-subtitle>
                          </v-card>
                        <v-card
                          class="pa-2 pa-first"
                          outlined
                          tile
                          v-if="filesEmpty"
                        > Перетащите файл или выберите его, кликнув на блоке
                        </v-card>
                      </v-col>
                      <v-col
                       cols="12"
                       sm="6"
                        class="file-coast-col"
                      >
                        <v-card
                            class="pa-2 file-load-card"
                            outlined
                            tile
                              v-if="coastedFiles.length>0"
                        >
                                               <v-card
                                                                          class="pa-2 file-name-card"
                                                                          outlined
                                                                          tile
                                                                            v-for="coastedFile in coastedFiles"
                                                                      >

                            <v-card-text>
                              <p class="display-1 text--primary">
                               {{coastedFile.fileName}}
                              </p>
                              </v-card-text>

                              <v-simple-table >
                                  <table>
                                      <thead>
                                        <tr>
                                          <th class="text-left">Name</th>
                                          <th class="text-left">Calories</th>
                                          <th></th>
                                        </tr>
                                      </thead>
                                      <tbody>
                                        <tr v-for="(mark, specialityName) in coastedFile.specialityNamesToMarks">
                                          <td>{{specialityName}}</td>
                                          <td><v-rating :value="mark*5" min="0"  background-color="indigo lighten-3" color="indigo"
                                           half-increments large></v-rating></td>
                                        </tr>
                                      </tbody>
                                      </table>
                                  </v-simple-table>
                                                                      </v-card>

                        </v-card>
                         <v-progress-circular
                                                      :size="300"
                                                      color="blue-grey"
                                                      indeterminate
                                                      class="loader"
                                                      v-if="loadFile"
                                                    ></v-progress-circular>
                       </v-col>
                    </v-row>
                    <input id="file" type="file" style="display:none" v-on:change="loadFilesFromInput"/>
                  </v-container>

                   <v-dialog
                                                   v-model="dialogVisible"
                                                   width="500"
                                                 >

                                                   <v-card class="white-card">
                                                     <v-card-title
                                                       class="headline lighten-2"
                                                       primary-title
                                                     >
                                                      Ошибка
                                                     </v-card-title>

                                                     <v-card-text>
                                                      Файл имеет недопустимое расширение, допустимые расширения: docx, pdf, zip.
                                                     </v-card-text>

                                                     <v-divider></v-divider>

                                                     <v-card-actions>
                                                       <v-spacer></v-spacer>
                                                       <v-btn
                                                         color="primary"
                                                         text
                                                         @click="dialog = false"
                                                       >
                                                         ОК
                                                       </v-btn>
                                                     </v-card-actions>
                                                   </v-card>
                                                 </v-dialog>
                                                 </v-app>
       </div>

   </body>
</html>