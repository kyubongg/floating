import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

//샘플 코드는 컴포지션 스타일로 되어있다!
export const useTodosStore = defineStore('todos', () => {
  //state
  let id = 1; //고유한 식별자를 하나 만들겠다!
  //지금은 우리가 임시로 todos를 직접 작성을 했지만..
  //추후에는 REST API로 서버에서 받아올 목록이다!
  const todos = ref([
    {
      id: id++,
      text: "실습하기",
      isDone: false,
    },
    {
      id: id++,
      text: "기상하기",
      isDone: true,
    },
    {
      id: id++,
      text: "식사하기",
      isDone: false,
    },
  ])
  //getters
  const doneTodoCount = computed(() => {
    return todos.value.filter((todo) => { return todo.isDone }).length
  })


  //atcions
  const addTodo = function (text) {
    todos.value.push({
      id: id++,
      text, //es6문법으로 키와벨류가 동일하다?->축약형태로 작성이 가능
      isDone: false
    })
  }

  const deleteTodo = function (id) {
    // console.log(id) 아이디가 잘넘어오는 것 확인했다!

    const idx = todos.value.findIndex((todo) => {
      return todo.id === id
    })

    //idx를 뽑았다! id가 만약에 todos 목록에 없어 -> -1
    //있어! 해당 인덱스번호 => 없을리가 없어 ㅎ
    todos.value.splice(idx, 1)
  }
  //위의 코드는 index를 찾아 원본배열에서 제거하는 방식
  //아래의 코드 필터를 통해 요건을 갖춘 요소들만 새로운 배열로 만들어서 덮어버리기

  //필터를 이용해서 deleteTodo를 완성할 수 있을까용?
  const deleteTodo2 = function (id) {
    todos.value = todos.value.filter(todo => todo.id !== id)
  }

  const updateTodo = function (id) {
    //방법은 엄청나게 많다.
    //교안에서는 map 함수를 썼다.
    //순수한 for문을 이용해서도 처리할 수 있다.

    const findTodo = todos.value.find((todo) => {
      return todo.id === id
    })

    if (findTodo) {
      findTodo.isDone = !findTodo.isDone
    }
  }




  return { todos, addTodo, deleteTodo, deleteTodo2, updateTodo, doneTodoCount }
}, {
  persist: true,
},


)
