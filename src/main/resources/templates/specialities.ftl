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
      <link href="/css/specialities.css" rel="stylesheet">
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
                    el: '#all-specialities',
                    vuetify: new Vuetify(),
                    data: {
                         addSpecialityDialog: false,
                         specialityName: '',
                         employees: 'На данный момент есть открытые вакансии',
                         add: false,
                         serverItemsLength:0,
                         specialities: [],
                         items: ['На данный момент есть открытые вакансии', 'На данный момент нет открытых вакансий'],
                         headers: [
                           {
                                                                                                           text: '№ п/п',
                                                                                                           align: 'left',
                                                                                                           sortable: false,
                                                                                                           value: 'rowNumber'
                                                                                        },
                                  {
                                                                                  text: 'Наименование',
                                                                                  align: 'left',
                                                                                  sortable: false,
                                                                                  value: 'specialityName'
                                                               },
                                                               {
                                                                                                                                                                          text: 'Наличие актуальных вакансий',
                                                                                                                                                                          align: 'left',
                                                                                                                                                                          sortable: false,
                                                                                                                                                                          value: 'employees'
                                                                                                                                                       },
                                  { text: '', value: 'action', sortable: false }
                               ],
                        removeDialog:false,
                        loading:true,
                        options:{
                                  rowsPerPage:5,
                                  load: true
                                 },
                         search: '',
                         removing: false,
                         specialityId: 0,
                         edit: false,
                         editSpecialityDialog: false,
                         loadNetworkDialog: false,
                         loadNetworkFile: false,
                         loadedFile: false,
                         currentSpecialityId: 0,
                         errFile: '',
                         successFile: ''
                    },
                    watch: {
                            options: {
                                handler() {
                                    this.getDataFromApi();
                                },
                                deep: true
                            }
                   },
                    mounted () {
                              this.getDataFromApi();
                    },
                    methods:{
                    changeFileInput(e){
                                                  this.loadedFile =  e;
                                               },
                                               saveNetwork(){
                                                   this.errFile = '';
                                                   this.successFile = '';
                                                   let specialityId = this.currentSpecialityId;

                                                   if(this.loadedFile){
                                                                                   let fileExtension = this.loadedFile.name.split('.');
                                                                                   fileExtension = fileExtension[fileExtension.length - 1].toLowerCase();

                                                                                   if(fileExtension!='json'){
                                                                                      this.errFile = "Недопустимый формат файла. Допустимые форматы: json.";
                                                                                      return;
                                                                                   }

                                                   let formData = new FormData();
                                                   formData.append('file', this.loadedFile);
                                                   this.loadNetworkFile=true;

                                                   fetch('/file/load_neural_network_to_file?speciality_id=' + specialityId, {
                                                                                                          method:'POST',
                                                                                                          body: formData
                                                                                                         })
                                                                                                         .then(res => res.json())
                                                                                                         .then(res => {
                                                                                                                       this.loadNetworkFile=false;
                                                                                                                       this.successFile = "Нейронная сеть успешно загружена";
                                                                                                                      })
                                                                                                         .catch(e => {
                                                                                                                        this.loadNetworkFile=false;
                                                                                                                      });
                                                                                                                      } else {
                                                                                                                       this.errFile = "Пожалуйста, выберите файл";
                                                                                                                      }
                                               },
                            getDataFromApi () {

                                    this.loading = true;

                                    let search = this.search;
                                    const { page, rowsPerPage } = this.options;


                                           let dataTableOptions = {
                                            page : page,
                                            rowsPerPage : rowsPerPage,
                                            searchVal: search
                                           };

                                           axios({
                                                          url: '/speciality/all_for_editing',
                                                          method:'post',
                                                          data: dataTableOptions
                                                 })
                                                 .then(response=>{
                                                          if(response.status == 200){
                                                          console.log(this.specialities);
                                                            this.specialities = response.data.data;

                                                            this.serverItemsLength  = response.data.totalRecords;
                                                            this.loading = false;

                                                          }
                                                 })
                                                 .catch(error=>{
                                                          if(error.response){

                                                          }
                                                 })
                                                 .finally(() => this.loading = false);
                            },
                            searchInTable(){
                               this.getDataFromApi();
                            },
                            removeSpeciality(){
                                let specialityId = this.specialityId;
                                this.removing = true;

                                                                fetch('/speciality/remove?specialityId='+specialityId, {
                                                                                                                       method:'POST'
                                                                                                                      })
                                                                                                                      .then(res => res.json())
                                                                                                                      .then(res => {
                                                                                                                                    this.removing = false;
                                                                                                                                    this.removeDialog = false;
                                                                                                                                    console.log(res);
                                                                                                                                      this.options.page = 1;
                                                                                                                                      this.getDataFromApi();
                                                                                                                                   })
                                                                                                                      .catch(e => {
                                                                                                                                     this.removing = false;
                                                                                                                                     console.error(JSON.stringify(e.message));
                                                                                                                                   });
                            },
                            addSpeciality(){
                            this.add = true;
                            let specialityName = this.specialityName;
                            let employees = this.employees=='На данный момент есть открытые вакансии';

                              axios({
                                                                                      url: '/speciality/add',
                                                                                      method:'post',
                                                                                      data: {
                                                                                       specialityName: specialityName,
                                                                                       employees: employees
                                                                                      }
                                                                             })
                                                                             .then(response=>{
                                                                                     this.add = false;
                                                                                      this.options.page = 1;
                                                                                                                                                                                                                            this.getDataFromApi();
                                                                                                                                                                                                                            this.addSpecialityDialog = false;
                                                                             })
                                                                             .catch(error=>{
                                                                                      if(error.response){
                                                                                         this.add = false;
                                                                                      }
                                                                             })
                                                                             .finally(() =>  this.add = false);
                            },
                            showRemoveDialog(specialityId){
                              this.currentSpecialityId = specialityId;
                              this.removeDialog = true;
                            },
                            showEditDialog(item){
                                this.editSpecialityDialog = true;
                                this.specialityName = item.specialityName;
                                this.employees = item.employees?'На данный момент есть открытые вакансии':'На данный момент нет открытых вакансий';
                                this.specialityId = item.id;
                            },
                            loadNeuralNetwork(item){
                                  this.loading = true;

                                  axios({
                                                                                                                                                  url: '/file/load_neural_network_to_file?speciality_id='+item.id,
                                                                                                                                                  method:'post'
                                                                                                                                         })
                                                                                                                                         .then(response=>{
                                                                                                                                            this.loading = false;

                                                                                                                                            let url = new URL(window.location.origin + '/download/resume');
                                                                                                                                            url.searchParams.append('file_path', response.data.hrFullName);
                                                                                                                                            url.searchParams.append('file_name', item.specialityName+'.json');
                                                                                                                                            window.location = url.href;
                                                                                                                                         });
                            },
                            uploadNeuralNetwork(item){
                              this.loadNetworkDialog = true;
                              this.currentSpecialityId = item.id;
                            },
                            editSpeciality(){
                             this.edit = true;
                                                        let specialityName = this.specialityName;
                                                        let employees = this.employees=='На данный момент есть открытые вакансии';
                                                        let id = this.specialityId;

                                                          axios({
                                                                                                                  url: '/speciality/update_speciality',
                                                                                                                  method:'post',
                                                                                                                  data: {
                                                                                                                   specialityName: specialityName,
                                                                                                                   employees: employees,
                                                                                                                   id: id
                                                                                                                  }
                                                                                                         })
                                                                                                         .then(response=>{
                                                                                                                 this.edit = false;
                                                                                                                  this.specialities.forEach(function(item){
                                                                                                                                                                                                                                                                   if(item.id == id){
                                                                                                                                                                                                                                                                     item.specialityName = specialityName
                                                                                                                                                                                                                                                                     item.employees = employees;
                                                                                                                                                                                                                                                                   }
                                                                                                                                                                                                                                                                 });

                                                                                                                                                                                                                                                        this.editSpecialityDialog = false;
                                                                                                         })
                                                                                                         .catch(error=>{
                                                                                                                  if(error.response){
                                                                                                                     this.edit = false;
                                                                                                                  }
                                                                                                         })
                                                                                                         .finally(() =>  this.edit = false);
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
       <div id="all-specialities">
       <v-app>
        <v-container class="grey lighten-5">
                           <v-row no-gutters>
                             <v-col
                                                          cols="4"
                                                          sm="4"
                                                          :offset="7"
                                                        >
                                                         <v-text-field label="Название специальности" class="mt-7" v-model="search"></v-text-field>
                                                         <v-btn small color="primary" :right=true :large=true class="float-right mt-5" @click="searchInTable">Поиск</v-btn>
                                                        <v-btn small color="primary" :right=true :large=true class="float-right mt-5 mr-5" @click="addSpecialityDialog = true">Добавить специальность</v-btn>
                                                        </v-col>
        <v-col
                               cols="12"
                               sm="10"
                               :offset="1"
                             >
        <v-data-table
          :headers="headers"
          :items="specialities"
          :items-per-page="5"
          :server-items-length="serverItemsLength"
          :options.sync="options"
          :loading="loading"
          class="elevation-1 specialities-table"
          id="specialities_table"
        >
         <template v-slot:item.employees="{ item }">
                  <span  v-if="item.employees">На данный момент есть открытые вакансии</span>
                   <span  v-if="!item.employees">На данный момент нет открытых вакансий</span>
                </template>
        <template v-slot:item.action="{ item }">

                                                                                                                                                                         <v-icon class="red--text" middle @click="showRemoveDialog(item.id)">mdi-delete</v-icon>
                                                                                                                                                                         <v-icon class="primary--text" middle @click="showEditDialog(item)">mdi-update</v-icon>
                                                                                                                                                                         <v-icon class="green--text" middle @click="loadNeuralNetwork(item)">mdi-arrow-collapse-down</v-icon>
                                                                                                                                                                         <v-icon class="blue--text" middle @click="uploadNeuralNetwork(item)">mdi-arrow-collapse-up</v-icon>


                                                          </template>
        </v-data-table>
                                         </v-col>
                                         </v-row>
                                         </v-container>
                                         <v-dialog
                                                                                                                                                               v-model="editSpecialityDialog"
                                                                                                                                                               width="500"
                                                                                                                                                             >

                                                                                                                                                               <v-card>
                                                                                                                                                                       <v-card-title
                                                                                                                                                                         class="headline grey lighten-2"
                                                                                                                                                                         primary-title

                                                                                                                                                                       >
                                                                                                                                                                        Редактирование специальности
                                                                                                                                                                       </v-card-title>

                                                                                                                                                                       <v-card-text>
                                          <v-text-field
                                                                                                                                                                                                                                                                        label="Наименование"
                                                                                                                                                                                                                                                                        outlined
                                                                                                                                                                                                                                                                        v-model="specialityName"
                                                                                                                                                                                                                                                                      class="mt-1"></v-text-field>
                                                                                                                                                                                                                                                                       <v-select
                                                                                                                                                                                                                                                                                :items="items"
                                                                                                                                                                                                                                                                                label="Наличие актуальных вакансий"
                                                                                                                                                                                                                                                                                dense
                                                                                                                                                                                                                                                                                outlined
                                                                                                                                                                                                                                                                                v-model="employees"
                                                                                                                                                                                                                                                                              ></v-select>
                                                                                                                                                                                                                                                                      <v-card-text>

                                                                                                                                                                         </v-card-text>

                                                                                                                                                                       <v-divider></v-divider>

                                                                                                                                                                       <v-card-actions>
                                                                                                                                                                       <v-progress-circular
                                                                                                                                                                             indeterminate
                                                                                                                                                                             color="primary"
                                                                                                                                                                              v-if="edit"
                                                                                                                                                                           ></v-progress-circular>
                                                                                                                                                                         <v-spacer></v-spacer>
                                                                                                                                                                           <v-btn
                                                                                                                                                                                                                                                                                                   color="primary"
                                                                                                                                                                                                                                                                                                   text
                                                                                                                                                                                                                                                                                                   @click="editSpecialityDialog = false"
                                                                                                                                                                                                                                                                                                 >
                                                                                                                                                                                                                                                                                                   Отмена
                                                                                                                                                                                                                                                                                                 </v-btn>
                                                                                                                                                                         <v-btn
                                                                                                                                                                           color="primary"
                                                                                                                                                                           text
                                                                                                                                                                           :disabled = "specialityName==''"
                                                                                                                                                                           @click="editSpeciality()"
                                                                                                                                                                         >
                                                                                                                                                                           Изменить
                                                                                                                                                                         </v-btn>
                                                                                                                                                                       </v-card-actions>
                                                                                                                                                                     </v-card>
                                                                                                                                                               </v-dialog>
<v-dialog
                                                                                                                      v-model="addSpecialityDialog"
                                                                                                                      width="500"
                                                                                                                    >

                                                                                                                      <v-card>
                                                                                                                              <v-card-title
                                                                                                                                class="headline grey lighten-2"
                                                                                                                                primary-title

                                                                                                                              >
                                                                                                                               Добавление специальности
                                                                                                                              </v-card-title>

                                                                                                                              <v-card-text>
 <v-text-field
                                                                                                                                                                                                                               label="Наименование"
                                                                                                                                                                                                                               outlined
                                                                                                                                                                                                                               v-model="specialityName"
                                                                                                                                                                                                                             class="mt-1"></v-text-field>
                                                                                                                                                                                                                              <v-select
                                                                                                                                                                                                                                       :items="items"
                                                                                                                                                                                                                                       label="Наличие актуальных вакансий"
                                                                                                                                                                                                                                       dense
                                                                                                                                                                                                                                       outlined
                                                                                                                                                                                                                                       v-model="employees"
                                                                                                                                                                                                                                     ></v-select>
                                                                                                                                                                                                                             <v-card-text>

                                                                                                                                </v-card-text>

                                                                                                                              <v-divider></v-divider>

                                                                                                                              <v-card-actions>
                                                                                                                              <v-progress-circular
                                                                                                                                    indeterminate
                                                                                                                                    color="primary"
                                                                                                                                     v-if="add"
                                                                                                                                  ></v-progress-circular>
                                                                                                                                <v-spacer></v-spacer>
                                                                                                                                  <v-btn
                                                                                                                                                                                                                                                          color="primary"
                                                                                                                                                                                                                                                          text
                                                                                                                                                                                                                                                          @click="addSpecialityDialog = false"
                                                                                                                                                                                                                                                        >
                                                                                                                                                                                                                                                          Отмена
                                                                                                                                                                                                                                                        </v-btn>
                                                                                                                                <v-btn
                                                                                                                                  color="primary"
                                                                                                                                  text
                                                                                                                                  :disabled = "specialityName==''"
                                                                                                                                  @click="addSpeciality()"
                                                                                                                                >
                                                                                                                                  Добавить
                                                                                                                                </v-btn>
                                                                                                                              </v-card-actions>
                                                                                                                            </v-card>
                                                                                                                      </v-dialog>
                                                                                                                      <v-dialog
                                                                                                                                                                                     v-model="loadNetworkDialog"
                                                                                                                                                                                     width="500"
                                                                                                                                                                                   >
                                                                                                                                                                                     <v-card>
                                                                                                                                                                                             <v-card-title
                                                                                                                                                                                               class="headline grey lighten-2"
                                                                                                                                                                                               primary-title
                                                                                                                                                                                             >
                                                                                                                                                                                              Загрузка файла
                                                                                                                                                                                             </v-card-title>

                                                                                                                                                                                             <v-card-text>
                                                                                                                                                                                                 <v-file-input
                                                                                                                                                                                                     label="File input"
                                                                                                                                                                                                     filled
                                                                                                                                                                                                     class="mt-5"
                                                                                                                                                                                                     v-on:change="changeFileInput"
                                                                                                                                                                                                   ></v-file-input>
                                                                                                                                                                                             </v-card-text>

                                                                                                                                                                                             <v-divider></v-divider>

                                                                                                                                                                                             <v-card-actions>
                                                                                                                                                                                             <v-progress-circular
                                                                                                                                                                                                   indeterminate
                                                                                                                                                                                                   color="primary"
                                                                                                                                                                                                    v-if="loadNetworkFile"
                                                                                                                                                                                                 ></v-progress-circular>
                                                                                                                                                                                               <v-spacer></v-spacer>
                                                                                                                                                                                               <div style="color:red" v-if="errFile!=''">{{errFile}}</div>
                                                                                                                                                                                               <div style="color:green" v-if="successFile!=''">{{successFile}}</div>
                                                                                                                                                                                               <v-btn
                                                                                                                                                                                                                                                                          color="green"
                                                                                                                                                                                                                                                                          text
                                                                                                                                                                                                                                                                          @click="loadNetworkDialog=false"
                                                                                                                                                                                                                                                                        >
                                                                                                                                                                                                                                                                          Отмена
                                                                                                                                                                                                                                                                        </v-btn>
                                                                                                                                                                                               <v-btn
                                                                                                                                                                                                 color="primary"
                                                                                                                                                                                                 text
                                                                                                                                                                                                 @click="saveNetwork()"
                                                                                                                                                                                               >
                                                                                                                                                                                                 Загрузить
                                                                                                                                                                                               </v-btn>
                                                                                                                                                                                             </v-card-actions>
                                                                                                                                                                                           </v-card>
                                                                                                                                                                                     </v-dialog>
                                                                                                                        <v-dialog
                                                                                                                                                                                                                                            v-model="removeDialog"
                                                                                                                                                                                                                                            width="500"
                                                                                                                                                                                                                                          >

                                                                                                                                                                                                                                            <v-card>
                                                                                                                                                                                                                                                    <v-card-title
                                                                                                                                                                                                                                                      class="headline grey lighten-2"
                                                                                                                                                                                                                                                      primary-title

                                                                                                                                                                                                                                                    >
                                                                                                                                                                                                                                                     Удаление специальности
                                                                                                                                                                                                                                                    </v-card-title>

                                                                                                                                                                                                                                                    <v-card-text>
                                                                                                                                                                                                                                                      Вы уверены, что хотите удалить специальность (будет удалена нейронная сеть, связанная с ней и все оценки файлов)?
                                                                                                                                                                                                                                                    </v-card-text>

                                                                                                                                                                                                                                                    <v-divider></v-divider>

                                                                                                                                                                                                                                                    <v-card-actions>
                                                                                                                                                                                                                                                    <v-progress-circular
                                                                                                                                                                                                                                                          indeterminate
                                                                                                                                                                                                                                                          color="primary"
                                                                                                                                                                                                                                                           v-if="removing"
                                                                                                                                                                                                                                                        ></v-progress-circular>
                                                                                                                                                                                                                                                      <v-spacer></v-spacer>
                                                                                                                                                                                                                                                        <v-btn
                                                                                                                                                                                                                                                                                                                                                                                color="primary"
                                                                                                                                                                                                                                                                                                                                                                                text
                                                                                                                                                                                                                                                                                                                                                                                @click="removeDialog = false"
                                                                                                                                                                                                                                                                                                                                                                              >
                                                                                                                                                                                                                                                                                                                                                                                Отмена
                                                                                                                                                                                                                                                                                                                                                                              </v-btn>
                                                                                                                                                                                                                                                      <v-btn
                                                                                                                                                                                                                                                        color="danger"
                                                                                                                                                                                                                                                        text
                                                                                                                                                                                                                                                        @click="removeSpeciality()"
                                                                                                                                                                                                                                                      >
                                                                                                                                                                                                                                                        Удалить
                                                                                                                                                                                                                                                      </v-btn>
                                                                                                                                                                                                                                                    </v-card-actions>
                                                                                                                                                                                                                                                  </v-card>
                                                                                                                                                                                                                                            </v-dialog>
                                         </v-app>
       </div>
   </body>
</html>