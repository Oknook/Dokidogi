<script setup></script>

<template>
  <div class="petRegister">
    <div>반려동물 등록창</div>
    <form action="">
      <div>
        <label for="registerID">등록번호: </label>
        <input type="text" id="registerID" />
      </div>
      <div>
        <label for="birthday">생년월일: </label>
        <input type="text" id="birthday" />
      </div>
      <button type="submit">등록</button>
    </form>
  </div>
</template>

<style scoped>
.petRegister {
  height: 100%;
  text-align: center;
  display: flex;
  flex-direction: column;
  margin-top: 50%;
  gap: 10px;
}

.petRegister button {
  margin-top: 10px;
}
</style>
