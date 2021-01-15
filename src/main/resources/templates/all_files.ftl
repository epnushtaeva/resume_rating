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
      <link href="/css/file-all.css" rel="stylesheet">
      <script src="/js/vue.js"></script>
      <script src="/js/vuetify.js"></script>
      <script src="/js/axios.min.js"></script>
      <script>
       let _priorState = false;
              document.addEventListener('DOMContentLoaded', function(){
               new Vue({
                    el: '#toolbar',
                    vuetify: new Vuetify(),
                  });

               new Vue({
                    el: '#all-files',
                    vuetify: new Vuetify(),
                    data: {
                         currentHrFullName: '',
                         currentHrPost: '',
                         currentHrEmail: '',
                         currentHrPhone: '',
                         hrInfoDialog: false,
                         serverItemsLength:0,
                         learnExampleDialog: false,
                         headers: [
                                  {
                                    text: 'Файл',
                                    align: 'left',
                                    sortable: false,
                                    value: 'name'
                                  },
                                  {
                                                                      text: 'Принят на работу',
                                                                      align: 'left',
                                                                      sortable: false,
                                                                      value: 'hired'
                                  },
                                   {
                                                                                                        text: 'HR специалист',
                                                                                                        align: 'left',
                                                                                                        sortable: false,
                                                                                                        value: 'hr'
                                   },
                                  {
                                       text: 'Оценки',
                                       align: 'center',
                                       sortable: false,
                                       value: 'specialities'
                                  },
                                  { text: '', value: 'action', sortable: false },
                               ],
                        specialitiesHeaders: [
                              {
                                                 text: 'Специальность',
                                                 align: 'left',
                                                 sortable: false,
                                                 value: 'specialityName'
                              },
                              {
                                                 text: 'Оценка',
                                                 align: 'center',
                                                 sortable: false,
                                                 value: 'mark'
                              }
                        ],
                        specialitiesTableHtml:'<v-expansion-panels class="grey lighten-4 mt-3"><v-expansion-panel class="grey lighten-5">'+
                                                     '<v-expansion-panel-header>ФИЛЬТРЫ</v-expansion-panel-header>'+
                                                     '<v-expansion-panel-content>'+
                                                   '<v-container class="bordered pl-2 mt-2"><v-row no-gutters><v-col cols="4" sm="4"><v-toolbar-title class="grey--text text--darken-4 text-center mt-3">Специальность</v-toolbar-title></v-col>'+
                        '<v-col cols="8" sm="8"><v-toolbar-title class="grey--text text--darken-4 text-center mt-3">Оценка</v-toolbar-title></v-col></v-row> <v-row no-gutters><v-col cols="4" sm="4"><v-text-field label="Наименование" class="mt-3 mr-5" v-model="speciality_name"></v-text-field></v-col><v-col cols="4" sm="4"><v-select :items="mark_filters" menu-props="auto" label="Select"'+
                                                                                                                                                 ' hide-details single-line v-model="mark_filter_type"'+
                                                                                                                                               ' class="mt-3"></v-select></v-col><v-col cols="4" sm="4"><v-rating'+
                                                                                                                                                                                ' background-color="indigo lighten-3"'+
                                                                                                                                                                                ' color="indigo"'+
                                                                                                                                                                                ' half-increments'+
                                                                                                                                                                                ' medium'+
                                                                                                                                                                                ' class="mt-5" v-model="file_mark" min="0"></v-rating></v-col></v-row></v-container>'+
                                                                                                                                                                                '<v-row no-gutters><v-col  cols="12"  sm="12" ><v-btn small color="primary" :right=true :large=true class="float-right mt-2 mr-2" @click="setBestSpecialitiesFilters">Наиболее подходящие</v-btn><v-btn small color="primary" :right=true :large=true class="float-right mt-2 mr-2" @click="getDataFromApi">Поиск</v-btn></v-col></v-row> </v-expansion-panel-content></v-expansion-panel></v-expansion-panels>'+
                                                                                                                                                                                '<v-row no-gutters><v-col  cols="5"  sm="5" class="text-left"> <v-chip'+
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ' class="ma-2"'+
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ' :color="updateResultColor"'+
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ' text-color="white"'+
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       '>'+
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       '{{updateResultText}}'+
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       '</v-chip></v-col><v-col  cols="2"  sm="2" ><v-progress-circular'+
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ' indeterminate'+
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ' color="primary"'+
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     ' v-if="updateSpeciality"'+
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  '></v-progress-circular></v-col></v-row><v-data-table '+
                                                                       ' :headers="headers"'+
                                                                       ' :items="specialities"'+
                                                                       ' :items-per-page="5"'+
                                                                       ' :server-items-length="serverItemsLength"'+
                                                                       ' :options.sync="options"'+
                                                                       ' :loading="loading"'+
                                                                       ' class="elevation-1 specialities-table"'+
                                                                     '>'+
                                                                     '<template v-slot:item.mark="{ item }">' +
                                                                     '<v-rating'+
                                                                            ' :value="item.mark*5" min="0"'+
                                                                            ' background-color="indigo lighten-3"'+
                                                                            ' color="indigo"'+
                                                                            ' half-increments'+
                                                                            ' large'+
                                                                            ' @input="changeSpecialityRating($event, item.id)"'+
                                                                          '></v-rating>'+
                                                                          '</template>' +
                                                                      '</v-data-table>',
                        files: [],
                        fileId: 0,
                        loadedFile:'',
                        dialog:false,
                        makingLearnExample: false,
                        removeDialog:false,
                        loadFile:false,
                        loading:true,
                        startLoadSpecialities:false,
                        removingFile:false,
                        currentFileId: 0,
                        options:{
                                  rowsPerPage:5,
                                  load: true
                                 },
                         search: '',
                         filterSpecialityId: 0,
                         specialityItems: []
                    },
                    watch: {
                            options: {
                                handler() {
                                    this.getDataFromApi();
                                },
                                deep: true
                            },
                            showRemoveDialog(fileId){
                               this.currentFileId = fileId;
                               this.removeDialog = true;
                            },
                            startLoadSpecialities:    {
                                                handler() {
                                                         if(this.startLoadSpecialities){
                                                         console.log(this.startLoadSpecialities);
console.log(this.startLoadSpecialities);
                                                         for(let fileIndex = 0; fileIndex < this.files.length; fileIndex++){
                                                                              let fileId = this.files[fileIndex].id;
                                                                              let specialitiesForCurrentFileBlockId = '#specialities_table_container_' + fileId;
                                                                              let self = this;
                                                                              console.log(fileIndex);
                                                                              let isLastIndex = fileIndex>=(this.files.length-1);
                                                                              let parentFiles = this.files;

                                                                               new Vue({
                                                                                                                                   el: specialitiesForCurrentFileBlockId,
                                                                                                                                   vuetify: new Vuetify(),
                                                                                                                                   data: {
                                                                                                                                        serverItemsLength:0,
                                                                                                                                        parentFiles: parentFiles,
                                                                                                                                        headers: [
                                                                                                                                         {
                                                                                                                                                                                                                                                                                                                                                                         text: '№ п/п',
                                                                                                                                                                                                                                                                                                                                                                         align: 'left',
                                                                                                                                                                                                                                                                                                                                                                         sortable: false,
                                                                                                                                                                                                                                                                                                                                                                         value: 'rowNumber'
                                                                                                                                                                                                                                                                                          },
                                                                                                                                                  {
                                                                                                                                                                                                                                 text: 'Специальность',
                                                                                                                                                                                                                                 align: 'left',
                                                                                                                                                                                                                                 sortable: false,
                                                                                                                                                                                                                                 value: 'specialityName'
                                                                                                                                                  },
                                                                                                                                                  {
                                                                                                                                                                                                                                 text: 'Оценка',
                                                                                                                                                                                                                                 align: 'center',
                                                                                                                                                                                                                                 sortable: false,
                                                                                                                                                                                                                                 value: 'mark'
                                                                                                                                                  }
                                                                                                                                              ],
                                                                                                                                       specialities: [],
                                                                                                                                       learnExampleDialog: true,
                                                                                                                                       updateSpeciality:false,
                                                                                                                                       id: fileId,
                                                                                                                                       loaded:'',
                                                                                                                                       loading:false,
                                                                                                                                       makingLearnExample: false,
                                                                                                                                       mark_filters:[
                                                                                                                                           'Не выбран',
                                                                                                                                           'Равно',
                                                                                                                                           'Больше',
                                                                                                                                           'Больше либо равно',
                                                                                                                                           'Меньше',
                                                                                                                                           'Меньше либо равно'
                                                                                                                                       ],
                                                                                                                                       mark_filter_type:"Не выбран",
                                                                                                                                       speciality_name: '',
                                                                                                                                       updateResultText:'',
                                                                                                                                       updateResultColor:'#fafafa',
                                                                                                                                        componentKey: 0,
                                                                                                                                       file_mark:0,
                                                                                                                                        options:{
                                                                                                                                                 rowsPerPage:5
                                                                                                                                                },
                                                                                                                                        search: ''
                                                                                                                                   },
                                                                                                                                     watch: {
                                                                                                                                                               options: {
                                                                                                                                                                   handler() {
                                                                                                                                                                    console.log(this.options);

                                                                                                                                                                    if(this.loading){
                                                                                                                                                                          return;
                                                                                                                                                                    }

                                                                                                                                                                             this.getDataFromApi();
                                                                                                                                                                   },
                                                                                                                                                                   deep: true
                                                                                                                                                               }
                                                                                                                                                      },
                                                                                                                                                       mounted () {

                                                                                                                                                       },

                                                                                                                                    methods:{
                                                                                                                                    setBestSpecialitiesFilters(){
                                                                                                                                        this.file_mark = 4;
                                                                                                                                        this.mark_filter_type = 'Больше';

                                                                                                                                        this.getDataFromApi();
                                                                                                                                    },
                                                                                                                                                               getDataFromApi () {
                                                                                                                                                                console.log("mounting");
                                                                                                                                                               if(this.loading){
                                                                                                                                                                                                                                                                                                                                         return;
                                                                                                                                                                                                                                                                                                                                   }
                                                                                                                                                                      console.log(this.loading);
                                                                                                                                                                       this.loading = true;

                                                                                                                                                                       const { page, rowsPerPage } = this.options;
                                                                                                                                                                       let searchVal = this.speciality_name;
                                                                                                                                                                       let fileMark = this.file_mark/5;
                                                                                                                                                                       let fileMarkFilterType = 5;

                                                                                                                                                                       switch(this.mark_filter_type){
                                                                                                                                                                         case "Равно":
                                                                                                                                                                          fileMarkFilterType = 5;
                                                                                                                                                                          break;
                                                                                                                                                                         case "Больше":
                                                                                                                                                                          fileMarkFilterType = 1;
                                                                                                                                                                          break;
                                                                                                                                                                         case "Больше либо равно":
                                                                                                                                                                          fileMarkFilterType = 3;
                                                                                                                                                                          break;
                                                                                                                                                                         case "Меньше":
                                                                                                                                                                          fileMarkFilterType = 2;
                                                                                                                                                                          break;
                                                                                                                                                                         case "Меньше либо равно":
                                                                                                                                                                          fileMarkFilterType = 4;
                                                                                                                                                                          break;
                                                                                                                                                                           case "Не выбран":
                                                                                                                                                                                                                                                                                                                                                    fileMarkFilterType = 6;
                                                                                                                                                                                                                                                                                                                                                    break;
                                                                                                                                                                       }

                                                                                                                                                                       if(page < 0){
                                                                                                                                                                         page = 1;
                                                                                                                                                                       }


                                                                                                                                                                              let dataTableOptions = {
                                                                                                                                                                                   page : 1,
                                                                                                                                                                                   rowsPerPage : rowsPerPage,
                                                                                                                                                                                   searchVal : searchVal,
                                                                                                                                                                                   fileId: this.id,
                                                                                                                                                                                   fileMarkFilterType:fileMarkFilterType,
                                                                                                                                                                                   mark: fileMark
                                                                                                                                                                              };

                                                                                                                                                                              axios({
                                                                                                                                                                                             url: '/speciality/all',
                                                                                                                                                                                             method:'post',
                                                                                                                                                                                             data: dataTableOptions
                                                                                                                                                                                    })
                                                                                                                                                                                    .then(response=>{

                                                                                                                                                                                             if(response.status == 200){


                                                                                                                                                                                               this.specialities = response.data.data;
                                                                                                                                                                                               this.serverItemsLength  = response.data.totalRecords;
 this.loading = false; let y = self.files[0].id; self.files[0].id=0;self.files[0].id=y;  if(isLastIndex){self.startLoadSpecialities=false;}

                                                                                                                                                                                             }
                                                                                                                                                                                    })
                                                                                                                                                                                    .catch(error=>{this.loading = false
                                                                                                                                                                                             if(error.response){
                                              this.loading = false;
                                                                                                                                                                                             }
                                                                                                                                                                                    })
                                                                                                                                                                                    .finally(() => this.loading = false);
                                                                                                                                                               },
                                                                                                                                                               changeSpecialityRating(e, specialityMarkId){ console.log(e);
                                                                                                                                                                 let mark = e/5;
                                                                                                                                                                 console.log(mark);
                                                                                                                                                                 this.updateSpeciality=true;

                                                                                                                                                                   axios({
                                                                                                                                                                                                                                                                                                                                                              url: '/speciality/update?specialityMarkId=' + specialityMarkId + '&mark=' + mark,
                                                                                                                                                                                                                                                                                                                                                              method:'post'

                                                                                                                                                                                                                                                                                                                                                     })
                                                                                                                                                                                                                                                                                                                                                     .then(response=>{
 this.updateSpeciality=false;
 this.updateResultText='Данные успешно обновлены';
 this.updateResultColor='green';
                                                                                                                                                                                                                                                                                                                                                              if(response.status == 200){
 if(response.hrFullName){ let fileId = this.id;
                                                                                                                this.parentFiles.forEach(function(item){
                                                                                                                                                 if(item.id == fileId){
                                                                                                                                                   item.userId = response.hrId;
                                                                                                                                                   item.hr = response.hrFullName;
                                                                                                                                                 }
                                                                                                                                               });

                                                                                                               }

                                                                                                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                                                                                                     })
                                                                                                                                                                                                                                                                                                                                                     .catch(error=>{
                                                                                                                                                                                                                                                                                                                                                      this.updateSpeciality=false;
                                                                                                                                                                                                                                                                                                                                                       this.updateResultText='Ошибка обновления данных';
                                                                                                                                                                                                                                                                                                                                                       this.updateResultColor='red';
                                                                                                                                                                                                                                                                                                                                                              if(error.response){

                                                                                                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                                                                                                     });
                                                                                                                                                               }
                                                                                                                                    }
                                                                                                                                   });
                                                                        }
                                 }
                                 }
                            }
                   },
                    mounted () {
                              this.initSpecialitiesList();
                              this.getDataFromApi();
                    },
                     updated () {
                          if (!_priorState) {
                                                  _priorState = {dialog: this._data['dialog'], removeDialog: this._data['removeDialog'], learnExampleDialog: this._data['learnExampleDialog'],
                                                  removingFile: this._data['removingFile'], makingLearnExample: this._data['makingLearnExample'], loadFile: this._data['loadFile'], filterSpecialityId: this._data['filterSpecialityId']};
                                                 }


                                                if(_priorState['dialog'] != this._data['dialog'] || _priorState['removeDialog'] != this._data['removeDialog'] || _priorState['learnExampleDialog'] != this._data['learnExampleDialog'] ||
                                                _priorState['makingLearnExample'] != this._data['makingLearnExample'] || _priorState['removingFile'] != this._data['removingFile'] ||  _priorState['loadFile'] != this._data['loadFile'] ||  _priorState['filterSpecialityId'] != this._data['filterSpecialityId']){
                                                    _priorState = {dialog: this._data['dialog'], removeDialog: this._data['removeDialog'], learnExampleDialog: this._data['learnExampleDialog'],
                                                    removingFile: this._data['removingFile'], makingLearnExample: this._data['makingLearnExample'], loadFile: this._data['loadFile'], filterSpecialityId: this._data['filterSpecialityId']};
                                                    return;
                                                }

                                                     _priorState = {dialog: this._data['dialog'], removeDialog: this._data['removeDialog'],learnExampleDialog: this._data['learnExampleDialog'],
                                                     removingFile: this._data['removingFile'], makingLearnExample: this._data['makingLearnExample'], loadFile: this._data['loadFile'] , filterSpecialityId: this._data['filterSpecialityId']};
                                                     if(this.files.length>0 && !this.startLoadSpecialities && !this.loading && !this.removeDialog && !this.dialog && !this.learnExampleDialog ){
                                                         this.startLoadSpecialities=true;
                                                     }
                     },
                    methods:{
                    initSpecialitiesList(){
                     axios({
                                                                              url: '/speciality/list',
                                                                              method:'post'
                                                                     })
                                                                     .then(response=>{
                                                                              if(response.status == 200){
                                                                              this.specialityItems = [
                                                                                {id:0, name:'--'}
                                                                              ];
                                                                              let t = this.specialityItems;
                                                                               response.data.forEach(function(sp){
                                                                                 t.push(sp);
                                                                               });
                                                                              }
                                                                     })
                                                                     .catch(error=>{
                                                                              if(error.response){

                                                                              }
                                                                     })
                    },
                            getDataFromApi () {
                             console.log('get data from api');
                                    this.loading = true;
                                    this.startLoadSpecialities = false;

                                    let search = this.search;
                                    let filterSpecialityId = this.filterSpecialityId;
                                    const { page, rowsPerPage } = this.options


                                           let dataTableOptions = {
                                            page : page,
                                            rowsPerPage : rowsPerPage,
                                            searchVal : search,
                                            specialityId: filterSpecialityId
                                           };

                                           axios({
                                                          url: document.getElementById('ajaxUrl').value,
                                                          method:'post',
                                                          data: dataTableOptions
                                                 })
                                                 .then(response=>{
                                                          if(response.status == 200){
                                                            this.files = response.data.data;

                                                            this.serverItemsLength  = response.data.totalRecords;
                                                            this.loading = false;

                                                            for(let fileIndex = 0; fileIndex < this.files.length; fileIndex++){
                                                                let fileId = this.files[fileIndex].id;
                                                             }
                                                          }
                                                 })
                                                 .catch(error=>{
                                                          if(error.response){

                                                          }
                                                 })
                                                 .finally(() => this.loading = false);
                            },
                            searchInFileTable(){
                            this.files = [];
                            this.startLoadSpecialities = false;
                            console.log("start load" + this.startLoadSpecialities);
                            console.log("loading" + this.loading);
                                if(this.options.page != 1){
                                this.options.page = 1;
                                } else {
                                this.options.load = !this.options.load;
                                }
                            },
                            loadHrInfo(id){
                               axios({
                                                                                      url: '/users/hr_info?id='+id,
                                                                                      method:'get'
                                                                             })
                                                                             .then(response=>{
                                                                             console.log(response);
                                                                                      if(response.status == 200){
                                                                                          this.currentHrFullName = response.data.fullName;
                                                                                          this.currentHrPost = response.data.post;
                                                                                          this.currentHrEmail = response.data.email;
                                                                                          this.currentHrPhone = response.data.phone;
                                                                                      }
                                                                             })
                                                                             .catch(error=>{
                                                                             console.log(error);
                                                                             });
                            },
                            updateFileHired(fileId){
                                let hired = false;

                                this.files.forEach(function(item){
                                  if(item.id == fileId){
                                     hired = item.hired;
                                  }
                                });

                                axios({
                                                                                                                    url: '/file/update_hired',
                                                                                                                    method:'post',
                                                                                                                    data: {
                                                                                                                      fileId: fileId,
                                                                                                                      hired: hired
                                                                                                                    }
                                                                                                           })
                                                                                                           .then(response=>{
                                                                                                           console.log(response);
                                                                                                               if(response.data.hrFullName){
                                                                                                                this.files.forEach(function(item){
                                                                                                                                                 if(item.id == fileId){
                                                                                                                                                   item.userId = response.data.hrId;
                                                                                                                                                   item.hr = response.data.hrFullName;
                                                                                                                                                 }
                                                                                                                                                 console.log(item);
                                                                                                                                               });

                                                                                                               }
                                                                                                           })
                                                                                                           .catch(error=>{
                                                                                                           console.log(error);
                                                                                                           });
                            },
                            changeFileInput(e){
                               this.loadedFile =  e;
                            },
                            sendLearnExampleFileToServer(){
                                  let formData = new FormData();


                                  formData.append('file', this.loadedFile);
                                  this.loadFile=true;

                                  fetch('/learn_example/load', {
                                                         method:'POST',
                                                         body: formData
                                                        })
                                                        .then(res => res.json())
                                                        .then(res => {
                                                         this.loadFile=false;
                                                            this.dialog = false;
                                                        this.files=[];
                                                                      this.searchInFileTable();
                                                                      console.log(res);

                                                                     })
                                                        .catch(e => {
                                                                       this.loadFile = false;
                                                                       console.error(JSON.stringify(e.message));
                                                                     });
                            },
                            showRemoveDialog(fileId){
                              this.currentFileId = fileId;
                               this.removeDialog = true;
                            },
                            removeFile(){
                                  this.removingFile = true;

                                 fetch('/file/remove?fileId='+this.currentFileId, {
                                                                                        method:'POST'
                                                                                       })
                                                                                       .then(res => res.json())
                                                                                       .then(res => {
                                                                                                     this.removingFile = false;
                                                                                                     this.removeDialog = false;
                                                                                                     console.log(res);
                                                                                                     this.searchInFileTable();

                                                                                                    })
                                                                                       .catch(e => {
                                                                                                      this.removingFile = false;
                                                                                                      console.error(JSON.stringify(e.message));
                                                                                                    });

                            },
                            showSetLearnExampleDialog(fileId){
                             this.currentFileId = fileId;
                             this.learnExampleDialog = true;
                            },
                            setLearnExampleFromFile(){
                              this.makingLearnExample = true;
                              let fileId = this.currentFileId;

                              fetch('/file/make_learn_example?file_id='+this.currentFileId, {
                                                                                     method:'POST'
                                                                                    })
                                                                                    .then(res => res.json())
                                                                                    .then(res => {
                                                                                       this.makingLearnExample = false;
                                                                                       this.learnExampleDialog = false;

                                                                                       this.files.forEach(function(file){
                                                                                          if(file.id == fileId){
                                                                                             file.learnExampleId = res.hrId;
                                                                                          }
                                                                                       });
                                                                                     })
                                                                                    .catch(e => {
                                                                                                  this.makingLearnExample = false;
                                                                                                   console.error(JSON.stringify(e.message));
                                                                                                 });
                            },
                            viewFile(item){
                               window.open('/download/view?file_url=/download/resume?file_path='+item.filePath + '%26file_name='+item.fileName, item.fileName,'width=600,height=400', "_blank")
                            },
                            downloadFile(item){
                               let url = new URL(window.location.origin + '/download/resume');
                               url.searchParams.append('file_path', item.filePath);
                               url.searchParams.append('file_name', item.fileName);
                               window.location = url.href;
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
       <div id="all-files">
       <v-app>
        <v-container class="grey lighten-5">
                           <v-row no-gutters>
                           <v-col
                                                                                     cols="3"
                                                                                     sm="3"
                                                                                     :offset="3"
                                                                                     class="mt-8"
                                                                                   >
                                                                                     <v-select
                                                                                             :items="specialityItems"
                                                                                             label="Получить наиболее подходящие резюме для специальности"
                                                                                             outlined
                                                                                              :item-value="item=>item.id"
                                                                                                                                                                                                                                                                                                                                                                                              :item-text="item=>item.name"
                                                                                             v-model="filterSpecialityId"
                                                                                           ></v-select>
                                                                                   </v-col>
                             <v-col
                                                          cols="4"
                                                          sm="4"
                                                          :offset="1"
                                                        >
                                                         <v-text-field label="Имя файла" class="mt-7" v-model="search"></v-text-field>
                                                         <v-btn small color="primary" :right=true :large=true class="float-right mt-5" @click="searchInFileTable">Поиск</v-btn>
                                                         <#if isCanAddFile == true>
                                                          <v-dialog
                                                               v-model="dialog"
                                                               width="500"
                                                             >
                                                           <template v-slot:activator="{ on }">
                                                                <v-btn small color="primary" :right=true :large=true class="float-right mt-5 mr-5"  v-on="on">Загрузить обучающий пример</v-btn>
                                                               </template>
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
                                                                              v-if="loadFile"
                                                                           ></v-progress-circular>
                                                                         <v-spacer></v-spacer>
                                                                         <v-btn
                                                                                                                                                    color="green"
                                                                                                                                                    text
                                                                                                                                                    @click="dialog=false"
                                                                                                                                                  >
                                                                                                                                                    Отмена
                                                                                                                                                  </v-btn>
                                                                         <v-btn
                                                                           color="primary"
                                                                           text
                                                                           @click="sendLearnExampleFileToServer()"
                                                                         >
                                                                           Загрузить
                                                                         </v-btn>
                                                                       </v-card-actions>
                                                                     </v-card>
                                                               </v-dialog>
                                                         </#if>
                                                        </v-col>
        <v-col
                               cols="12"
                               sm="11"
                               class="ml-12"
                             >
        <v-data-table
          :headers="headers"
          :items="files"
          :items-per-page="5"
          :server-items-length="serverItemsLength"
          :options.sync="options"
          :loading="loading"
          class="elevation-1 files-table"
          id="files_table"
        >
          <template v-slot:item.name="{item}">
                       <v-btn text @click.prevent="downloadFile(item)">{{item.fileName}}</v-btn>
                               </template>
               <template v-slot:item.specialities="{item}">
               <div v-html="specialitiesTableHtml" :id="'specialities_table_container_'+item.id"></div>
                       </template>
                       <template v-slot:item.hired="{item}">
                                      <v-checkbox :disabled="item.learnExample"
                                              v-model="item.hired" class="ml-10" @change="updateFileHired(item.id)"
                                            ></v-checkbox>
                                              </template>

                                               <template v-slot:item.action="{ item }">
                                               <v-icon class="red--text mr-0 pr-0" middle  @click="showRemoveDialog(item.id)" title="Удалить">mdi-delete</v-icon>
                                                <v-icon class="blue--text mr-0 pr-0 ml-0 pl-0" middle  @click="showSetLearnExampleDialog(item.id)" title="Создать обучающий пример из данного файла" v-if="!item.learnExample && !item.learnExampleId">mdi-arrow-right-circle-outline</v-icon>
                                                   <v-btn fab
                                                                 icon small style="width: 15px"
                                                                color="teal mr-0 pr-0 ml-0 pl-0" title="Просмотреть" @click.prevent="viewFile(item)"><v-icon dark>mdi-download-outline</v-icon></v-btn>
                                                  </template>
                                                  <template v-slot:item.hr="{ item }">
                                                                                                  <v-dialog v-if="item.hr && item.hr != ''"
                                                                                                                                                                                                        v-model="hrInfoDialog"
                                                                                                                                                                                                        width="500"
                                                                                                                                                                                                      >
                                                                                                                                                                                                    <template v-slot:activator="{ on }">
                                                                                                                                                                                                         <v-btn small text v-on="on" @click = "loadHrInfo(item.userId)">{{item.hr}}</v-btn>

                                                                                                                                                                                                        </template>
                                                                                                                                                                                                        <v-card>
                                                                                                                                                                                                                <v-card-title
                                                                                                                                                                                                                  class="headline grey lighten-2"
                                                                                                                                                                                                                  primary-title
                                                                                                                                                                                                                >
                                                                                                                                                                                                                 HR, загрузивший резюме
                                                                                                                                                                                                                </v-card-title>

                                                                                                                                                                                                                <v-card-text style="margin-top:10px">
                                                                                                                                                                                                                    <v-text-field
                                                                                                                                                                                                                               label="ФИО"
                                                                                                                                                                                                                               outlined
                                                                                                                                                                                                                               disabled
                                                                                                                                                                                                                               v-model="currentHrFullName"
                                                                                                                                                                                                                             ></v-text-field>
                                                                                                                                                                                                                             <v-text-field
                                                                                                                                                                                                                                                                                                                                                            label="Должность"
                                                                                                                                                                                                                                                                                                                                                            outlined
                                                                                                                                                                                                                                                                                                                                                              disabled
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           v-model="currentHrPost"
                                                                                                                                                                                                                                                                                                                                                          ></v-text-field>
                                                                                                                                                                                                                                                                                                                                                           <v-text-field
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      label="Email"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      outlined
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        disabled
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     v-model="currentHrEmail"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ></v-text-field>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     <v-text-field
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                label="Телефон"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                outlined
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  disabled
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               v-model="currentHrPhone"
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              ></v-text-field>
                                                                                                                                                                                                                </v-card-text>

                                                                                                                                                                                                                <v-divider></v-divider>

                                                                                                                                                                                                                <v-card-actions>

                                                                                                                                                                                                                  <v-spacer></v-spacer>
                                                                                                                                                                                                                  <v-btn
                                                                                                                                                                                                                                                                                             color="green"
                                                                                                                                                                                                                                                                                             text
                                                                                                                                                                                                                                                                                             @click="hrInfoDialog=false"
                                                                                                                                                                                                                                                                                           >
                                                                                                                                                                                                                                                                                            Закрыть
                                                                                                                                                                                                                                                                                           </v-btn>
                                                                                                                                                                                                                </v-card-actions>
                                                                                                                                                                                                              </v-card>
                                                                                                                                                                                                        </v-dialog>
                                                                                                                                                                                                        </template>
        </v-data-table>
                                         </v-col>
                                         </v-row>
                                         </v-container>

 <v-dialog
                                                                                                              v-model="removeDialog"
                                                                                                              width="500"
                                                                                                            >
                                                                                                              <v-card>
                                                                                                                      <v-card-title
                                                                                                                        class="headline grey lighten-2"
                                                                                                                        primary-title

                                                                                                                      >
                                                                                                                       Удаление файла
                                                                                                                      </v-card-title>

                                                                                                                      <v-card-text>
                                                                                                                        Вы уверены, что хотите удалить файл?
                                                                                                                      </v-card-text>

                                                                                                                      <v-divider></v-divider>

                                                                                                                      <v-card-actions>
                                                                                                                      <v-progress-circular
                                                                                                                            indeterminate
                                                                                                                            color="primary"
                                                                                                                             v-if="removingFile"
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
                                                                                                                          @click="removeFile()"
                                                                                                                        >
                                                                                                                          Удалить
                                                                                                                        </v-btn>
                                                                                                                      </v-card-actions>
                                                                                                                    </v-card>
                                                                                                              </v-dialog>
                                                                                                               <v-dialog
                                                                                                                                                                                                                            v-model="learnExampleDialog"
                                                                                                                                                                                                                            width="500"
                                                                                                                                                                                                                          >
                                                                                                                                                                                                                            <v-card>
                                                                                                                                                                                                                                    <v-card-title
                                                                                                                                                                                                                                      class="headline grey lighten-2"
                                                                                                                                                                                                                                      primary-title

                                                                                                                                                                                                                                    >
                                                                                                                                                                                                                                     Создание обучающего примера из файла резюме
                                                                                                                                                                                                                                    </v-card-title>

                                                                                                                                                                                                                                    <v-card-text>
                                                                                                                                                                                                                                      Вы уверены, что хотите создать обучающий пример? (Файл вместе с оценками по специальностям будет скопирован в таблицу с обучающими выборками)
                                                                                                                                                                                                                                    </v-card-text>

                                                                                                                                                                                                                                    <v-divider></v-divider>

                                                                                                                                                                                                                                    <v-card-actions>
                                                                                                                                                                                                                                    <v-progress-circular
                                                                                                                                                                                                                                          indeterminate
                                                                                                                                                                                                                                          color="primary"
                                                                                                                                                                                                                                           v-if="makingLearnExample"
                                                                                                                                                                                                                                        ></v-progress-circular>
                                                                                                                                                                                                                                      <v-spacer></v-spacer>
                                                                                                                                                                                                                                        <v-btn
                                                                                                                                                                                                                                                                                                                                                                color="primary"
                                                                                                                                                                                                                                                                                                                                                                text
                                                                                                                                                                                                                                                                                                                                                                @click="learnExampleDialog = false"
                                                                                                                                                                                                                                                                                                                                                              >
                                                                                                                                                                                                                                                                                                                                                                Отмена
                                                                                                                                                                                                                                                                                                                                                              </v-btn>
                                                                                                                                                                                                                                      <v-btn
                                                                                                                                                                                                                                        color="primary"
                                                                                                                                                                                                                                        text
                                                                                                                                                                                                                                        @click="setLearnExampleFromFile()"
                                                                                                                                                                                                                                      >
                                                                                                                                                                                                                                        Создать
                                                                                                                                                                                                                                      </v-btn>
                                                                                                                                                                                                                                    </v-card-actions>
                                                                                                                                                                                                                                  </v-card>
                                                                                                                                                                                                                            </v-dialog>

                                         </v-app>
       </div>

       <input style="display:none" value="${ajax_url}" id="ajaxUrl"/>
   </body>
</html>