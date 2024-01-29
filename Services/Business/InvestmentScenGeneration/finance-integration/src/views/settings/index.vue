
<template>
  <div class="profile">
      <el-form label-width="80px" :model="user">
      <el-form-item label="用户名" >
        <el-input v-model="user.username" ></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="user.password" show-password></el-input>
      </el-form-item>
    
      <el-form-item label="头像">
        <el-upload
          class="avatar-uploader"
          action="https://jsonplaceholder.typicode.com/posts/"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="user.avatar" :src="user.avatar" class="avatar" />
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
         <el-form-item label="性别">
        <el-radio-group v-model="user.gender">
          <el-radio label="男">男</el-radio>
          <el-radio label="女">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="个人简介">
        <el-input type="textarea" v-model="user.bio"></el-input>
      </el-form-item>
   
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: {
        username: "admin",
        password: "123456",
        avatar: "https://img1.imgtp.com/2023/04/07/MkEph025.jpg",
        bio: "这是我的个人简介",
        gender: "男",
      },
    };
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.user.avatar = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    submitForm() {
      // TODO: send user data to server
      this.$message.success("提交成功");
    },
    resetForm() {
      this.user = {
        username: "admin",
        password: "123456",
        avatar: "",
        bio: "这是我的个人简介",
        gender: "男",
      };
    },
  },
};
</script>

<style scoped>
.profile {
  width: 600px;
  margin: 100px auto;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
}
</style>

