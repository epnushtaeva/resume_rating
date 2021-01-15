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
      <link href="/css/settings.css" rel="stylesheet">
      <script src="/js/vue.js"></script>
      <script src="/js/vuetify.js"></script>
      <script src="/js/axios.min.js"></script>
       <script src="/js/vue-the-mask.min.js"></script>
      <script>
              document.addEventListener('DOMContentLoaded', function(){
               new Vue({
                    el: '#toolbar',
                    vuetify: new Vuetify(),
                  });

               new Vue({
                    el: '#settings',
                    vuetify: new Vuetify(),
                    data: {
                    tasksHeaders: [
                     {
                                                        text: 'Вид автоматически выполняемого задания',
                                                        align: 'left',
                                                        sortable: false,
                                                        value: 'taskType'
                                                      },
                                                      {
                                                                                          text: 'Дополнительная информация',
                                                                                          align: 'center',
                                                                                          sortable: false,
                                                                                          value: 'info'
                                                      },
                                                       {
                                                                                                                            text: 'Дата',
                                                                                                                            align: 'center',
                                                                                                                            sortable: false,
                                                                                                                            value: 'taskDate'
                                                       },
                                                      {
                                                           text: 'Время начала',
                                                           align: 'center',
                                                           sortable: false,
                                                           value: 'startTime'
                                                      },
                                                      {
                                                                                                                 text: 'Время окончания',
                                                                                                                 align: 'center',
                                                                                                                 sortable: false,
                                                                                                                 value: 'endTime'
                                                                                                            },
                                                                                                            {
                                                                                                                                                                                                                             text: 'Статус задания',
                                                                                                                                                                                                                             align: 'center',
                                                                                                                                                                                                                             sortable: false,
                                                                                                                                                                                                                             value: 'status'
                                                                                                                                                                                                                        },
                                                                                                                                                                                                                        {
                                                                                                                                                                                                                                                                                                                                                                                                                                                     text: '',
                                                                                                                                                                                                                                                                                                                                                                                                                                                     align: 'center',
                                                                                                                                                                                                                                                                                                                                                                                                                                                     sortable: false,
                                                                                                                                                                                                                                                                                                                                                                                                                                                     value: 'deleteButton'
                                                                                                                                                                                                                                                                                                                                                                                                                                                }
                    ],
                    tasks:[
                       {
                         taskType: 'Обучение нейронной сети',
                         info: '',
                         specialityName: 'Бухгалтер',
                         taskDate: '14.10.2020',
                         startTime: '11:30',
                         endTime: '13:30',
                         status: 'Задание успешно выполнено'
                       },
                       {
                                                taskType: 'Обучение нейронной сети',
                                                 info: '',
                                                specialityName: '',
                                                taskDate: '13.10.2020',
                                                startTime: '15:30',
                                                endTime: '16:30',
                                                status: 'Задание завершилось с ошибкой'
                                              },
                                               {
                                                                                              taskType: 'Обучение нейронной сети',
                                                                                              info: '',
                                                                                              specialityName: '',
                                                                                              taskDate: '14.10.2020',
                                                                                              startTime: '15:30',
                                                                                              endTime: '',
                                                                                              status: 'Задание ещё не выполнялось'
                                                                                            }
                    ],
                    loading: false,
                    options: {
                    page:1,
                    rowsPerPage:5
                    },
                    saveSettings: false,
                    specialities: [],
                    specialityId: null,
                    taskType: null,
                    taskTypes:[],
                    taskDate: new Date().toISOString().substr(0, 10),
                    startTime: new Date().getHours()+':'+new Date().getMinutes(),
                    showAddTaskDialog: false,
                    addTaskShow: false,
                    removeDialog: false,
                    removingTask: false
                    },
                    mounted(){
                        axios({
                                                                                 url: '/task_types/all',
                                                                                 method:'post',
                                                                        })
                                                                        .then(response=>{
                                                                                           console.log(response);
                                                                                            this.taskTypes = response.data;
                                                                                            this.taskType = this.taskTypes[0].id;
                                                                                         })
                                                                                         .catch(e => {
                                                                                           console.error(JSON.stringify(e.message));
                                                                                         });
                                                                                          axios({
                                                                                                                                                                          url: '/speciality/list',
                                                                                                                                                                          method:'post',
                                                                                                                                                                 })
                                                                                                                                                                 .then(response=>{
                                                                                                                                                                             this.specialities = [
                                                                                                                                                                               {
                                                                                                                                                                                 id: null,
                                                                                                                                                                                 name: '--'
                                                                                                                                                                               }
                                                                                                                                                                             ];
                                                                                                                                                                             let self = this;

                                                                                                                                                                             response.data.forEach(function(speciality){
                                                                                                                                                                                self.specialities.push(speciality);
                                                                                                                                                                             });
                                                                                                                                                                                  })
                                                                                                                                                                                  .catch(e => {
                                                                                                                                                                                    console.error(JSON.stringify(e.message));
                                                                                                                                                                                  });
                    },
                    watch: {
                                                options: {
                                                    handler() {
                                                        this.getDataFromApi();
                                                    },
                                                    deep: true
                                                }
                                                },
                    methods: {
                      getDataFromApi () {
                                                 console.log('get data from api');
                                                        this.loading = true;
                                                        const { page, rowsPerPage } = this.options


                                                               let dataTableOptions = {
                                                                page : page,
                                                                rowsPerPage : rowsPerPage
                                                               };

                                                               axios({
                                                                              url: '/tasks/all',
                                                                              method:'post',
                                                                              data: dataTableOptions
                                                                     })
                                                                     .then(response=>{
                                                                              if(response.status == 200){
                                                                                this.tasks = response.data.data;

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
                       getTaskStatusColor(taskStatus){
                          switch(taskStatus){
                            case 'Задание успешно выполнено':
                               return 'green';
                             case 'Задание завершилось с ошибкой':
                                              return 'red';
                              case 'Задание выполняется':
                                               return 'primary';
                             default:
                                return 'grey';
                          }
                       },
                       editSettings(){
                          this.saveSettings = true;
                          let email = document.getElementById('email').value;
 axios({
                                                          url: '/settings/edit',
                                                          method:'post',
                                                          data: {
                                                                                                                                      email: email
                                                                                                                                    }
                                                 })
                                                 .then(response=>{
                                                                     this.saveSettings = false;
                                                                  })
                                                                  .catch(e => {
                                                                    this.saveSettings = false;
                                                                    console.error(JSON.stringify(e.message));
                                                                  });
                       },
                       addTask(){
                        let taskType = this.taskType;
                        let specialityId = this.specialityId;
                        let taskDate = this.taskDate;
                        let startTime = this.startTime;

                        let task = {
                           taskTypeId: taskType,
                           specialityId: specialityId,
                           taskDate: taskDate,
                           startTime: startTime+':00',
                           taskStatusId: 1
                        };
                        this.addTaskShow = true;

                         axios({
                                                                                  url: '/tasks/add',
                                                                                  method:'post',
                                                                                  data: task
                                                                         })
                                                                         .then(response=>{
                                                                                             this.addTaskShow = false;
                                                                                             this.getDataFromApi();
                                                                                          })
                                                                                          .catch(e => {
                                                                                            this.addTaskShow = false;
                                                                                            console.error(JSON.stringify(e.message));
                                                                                          });
                       },
                       removeTask(taskId){
                          this.removingTask = true;

                           axios({
                                                                                                            url: '/tasks/delete',
                                                                                                            method:'post',
                                                                                                            data: {taskId: taskId}
                                                                                                   })
                                                                                                   .then(response=>{
                                                                                                                       this.removingTask = false;
                                                                                                                       this.removeDialog = false;
                                                                                                                       this.getDataFromApi();
                                                                                                                    })
                                                                                                                    .catch(e => {
                                                                                                                      this.removingTask = false;
                                                                                                                      this.removeDialog = false;
                                                                                                                      console.error(JSON.stringify(e.message));
                                                                                                                    });
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
       <div id="settings">
       <v-app>
        <v-container class="grey lighten-5">
                           <v-row no-gutters class="pr-8">
                           <v-col
                                   class="bordered rounded-lg mt-4 ml-4 mr-4"
                                   cols="12"
                                   sm="12"
                                   md="12"
                                 >

                                  <h2 class="text-center">Системные настройки</h2>
                                   <v-col

                                                                                                      cols="12"
                                                                                                      sm="12"
                                                                                                      md="6"
                                                                                                    >
                                   <v-text-field
                                     id="email"
                                     class="mt-1 mb-1"
                                     label="Email"
                                     outlined
                                     value="${email}"
                                   ></v-text-field>
                                   </v-col>
                                       <v-col class="pr-4"

                                                                                                                                                                                                            cols="12"
                                                                                                                                                                                                            sm="12"
                                                                                                                                                                                                            md="12"
                                                                                                                                                                                                          >
                                                                       <v-progress-circular
                                                                                                                             :size="300"
                                                                                                                             color="blue-grey"
                                                                                                                             indeterminate
                                                                                                                             class="loader"
                                                                                                                             v-if="saveSettings"
                                                                                                                           ></v-progress-circular>
                                                                       <v-btn small color="primary" :right=true :large=true class="float-right mt-5 mb-5" @click.prevent="editSettings">Сохранить</v-btn>
                                                                       </v-col>
                                 </v-col>
                                   <v-col
                                                                class="bordered rounded-lg mt-4 ml-4 mr-4"
                                                                    cols="12"
                                                                    sm="12"
                                                                    md="12"
                                                                  >
                                                                  <h2 class="text-center">Расписание автоматических заданий</h2>
                                                                   <v-col

                                                                                                                                      cols="12"
                                                                                                                                      sm="12"
                                                                                                                                      md="12"
                                                                                                                                    >
 <v-btn small color="primary" :right=true :large=true class="float-right mt-5 mb-5" @click.prevent = "showAddTaskDialog = true">Добавить задание</v-btn>
 </v-col>
 <v-col

                                                                                                                                       cols="12"
                                                                                                                                       sm="12"
                                                                                                                                       md="12"
                                                                                                                                     >
                                                                       <v-data-table
                                                                              :headers="tasksHeaders"
                                                                              :items="tasks"
                                                                              :items-per-page="5"
                                                                              :server-items-length="6"
                                                                              :options.sync="options"
                                                                              :loading="loading"
                                                                              class="elevation-1 tasks-table ml-2 mr-2 mb-4"
                                                                              id="tasks_table"
                                                                            >

                                                                            <template v-slot:item.status="{item}">
                                                                                <v-chip
                                                                                      class="ma-2"
                                                                                      :color="getTaskStatusColor(item.status)"
                                                                                    >
                                                                                      {{item.status}}
                                                                                    </v-chip>
                                                                                    </template>
                                                                                        <template v-slot:item.info="{item}">

                                                                                                                                                                  <span v-if="item.taskType=='Обучение нейронной сети' && item.specialityName!=null">Специальность: {{item.specialityName}}</span>
                                                                                                                                                                </template>
                                                                                         <template v-slot:item.deleteButton="{item}">
                                                                                              <v-dialog
                                                                                                                                                                                                           v-model="removeDialog"
                                                                                                                                                                                                           width="500"
                                                                                                                                                                                                         >
                                                                                                                                                                                                       <template v-slot:activator="{ on }">
                                                                                                                                                                                                          <v-icon class="red--text" middle  v-on="on">mdi-delete</v-icon>
                                                                                                                                                                                                         </template>
                                                                                                                                                                                                           <v-card>
                                                                                                                                                                                                                   <v-card-title
                                                                                                                                                                                                                     class="headline grey lighten-2"
                                                                                                                                                                                                                     primary-title
                                                                                                                                                                                                                   >
                                                                                                                                                                                                                    Удаление задания
                                                                                                                                                                                                                   </v-card-title>

                                                                                                                                                                                                                   <v-card-text>
                                                                                                                                                                                                                     Вы уверены, что хоите удалить задание?
                                                                                                                                                                                                                   </v-card-text>

                                                                                                                                                                                                                   <v-divider></v-divider>

                                                                                                                                                                                                                   <v-card-actions>
                                                                                                                                                                                                                   <v-progress-circular
                                                                                                                                                                                                                         indeterminate
                                                                                                                                                                                                                         color="primary"
                                                                                                                                                                                                                          v-if="removingTask"
                                                                                                                                                                                                                       ></v-progress-circular>
                                                                                                                                                                                                                     <v-spacer></v-spacer>
                                                                                                                                                                                                                     <v-btn
                                                                                                                                                                                                                       color="danger"
                                                                                                                                                                                                                       text
                                                                                                                                                                                                                       @click="removeTask(item.id)"
                                                                                                                                                                                                                     >
                                                                                                                                                                                                                       Удалить
                                                                                                                                                                                                                     </v-btn>
                                                                                                                                                                                                                   </v-card-actions>
                                                                                                                                                                                                                 </v-card>
                                                                                                                                                                                                           </v-dialog>
                                                                                         </template>
                                                                            </v-data-table>
                                                                            </v-col>
                                                                  </v-col>
                                         </v-row>
                                         </v-container>
                                            <v-dialog
                                                                                            v-model="showAddTaskDialog"
                                                                                            width="600"
                                                                                          >
                                                                                            <v-card class="white-card">
                                                                                              <v-card-title
                                                                                                class="headline lighten-2 text--center pl-12"
                                                                                                primary-title
                                                                                              >
                                                                                               Добавление автоматического задания
                                                                                              </v-card-title>


                                                                                                    <v-col
                                                                                                         class="d-flex"
                                                                                                         cols="12"
                                                                                                         sm="6"
                                                                                                         offset-md="2"
                                                                                                       >
                                                                                                         <v-select
                                                                                                           :items="taskTypes"
                                                                                                             item-text="name"
                                                                                                           item-value="id"
                                                                                                           label="Тип задания"
                                                                                                           outlined
                                                                                                           v-model="taskType"
                                                                                                         ></v-select>
                                                                                                       </v-col>
                                                                                                       <v-col
                                                                                                                                                                                                                class="d-flex"
                                                                                                                                                                                                                cols="12"
                                                                                                                                                                                                                sm="6"
                                                                                                                                                                                                                offset-md="2"
                                                                                                                                                                                                              >
                                                                                                                                                                                                                <v-select
                                                                                                                                                                                                                  :items="specialities"
                                                                                                                                                                                                                  item-text="name"
                                                                                                                                                                                                                            item-value="id"
                                                                                                                                                                                                                  label="Специальность"
                                                                                                                                                                                                                  outlined
                                                                                                                                                                                                                  v-model="specialityId"
                                                                                                                                                                                                                ></v-select>
                                                                                                                                                                                                              </v-col>
                                                                                                                                                                                                              <v-col
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  class="d-flex"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  cols="12"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  sm="6"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  offset-md="2"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                >
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                <label>Дата запуска:</label>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                </v-col>
                                                                                                        <v-col
                                                                                                                                                                                                                                                                                                                        class="d-flex"
                                                                                                                                                                                                                                                                                                                        cols="12"
                                                                                                                                                                                                                                                                                                                        sm="9"
                                                                                                                                                                                                                                                                                                                        offset-md="2"
                                                                                                                                                                                                                                                                                                                      >
                                                                                                                                                                                                                                                                                                                      <v-date-picker
                                                                                                                                                                                                                                                                                                                            v-model="taskDate"
                                                                                                                                                                                                                                                                                                                            class="mt-4"
                                                                                                                                                                                                                                                                                                                            :min="taskDate"
                                                                                                                                                                                                                                                                                                                          ></v-date-picker>
                                                                                                                                                                                                                                                                                                                      </v-col>
                                                                                                                                                                                                                                                                                                                      <v-col
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    class="d-flex"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    cols="12"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    sm="6"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    offset-md="2"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  >
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  <label>Время запуска:</label>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  </v-col>
                                                                                                                                                                                                                                                                                                                      <v-col
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              class="d-flex"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              cols="12"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              sm="9"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              offset-md="2"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            >

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             <v-row>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             <v-time-picker
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  v-model="startTime"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  :min="startTime"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  class="mt-4"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  format="24hr"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ></v-time-picker>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                </v-row>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            </v-col>



                                                                                              <v-divider></v-divider>

                                                                                              <v-card-actions>
                                                                                                <v-spacer></v-spacer>
<v-progress-circular
                                                                                                                             :size="300"
                                                                                                                             color="blue-grey"
                                                                                                                             indeterminate
                                                                                                                             class="loader"
                                                                                                                             v-if="addTaskShow"
                                                                                                                           ></v-progress-circular>
                                                                                                <v-btn
                                                                                                  color="primary"
                                                                                                  @click.prevent="addTask()"
                                                                                                >
                                                                                                  Добавить
                                                                                                </v-btn>
                                                                                                <v-btn @click.prevent="showAddTaskDialog=false"
                                                                                                                                                                                                                                                                                                  color="error"
                                                                                                                                                                                                                                                                                                >
                                                                                                                                                                                                                                                                                                  Отмена
                                                                                                                                                                                                                                                                                                </v-btn>
                                                                                              </v-card-actions>
                                                                                            </v-card>
                                                                                          </v-dialog>
                                         </v-app>
       </div>
   </body>
</html>