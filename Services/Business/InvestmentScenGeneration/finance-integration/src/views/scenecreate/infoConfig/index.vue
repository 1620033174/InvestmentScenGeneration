<script>
import store from "@/store";

export default({
  data(){
    return {
      themes: [
        {value: "科技", label: "科技"},
        {value: "金融", label: "金融"},
        {value: "教育", label: "教育"},
        {value: "医疗", label: "医疗"},
      ],
      tags: [
        {id: 1, name: "创新"},
        {id: 2, name: "高效"},
        {id: 3, name: "可靠"},
        {id: 4, name: "安全"},
      ],
      tagsValue: [],
      selectedTags: [],
      addTagDialogVisible: false,
      addTagForm: {
        name: ""
      },
      editTagDialogVisible: false,
      editTagForm: {
        name: ""
      },
      form: {
        sceneName: "",
        theme: "",
        description: "",
        tags: []
      },
      staticDialogVisible: false
    }
  },
  methods:{
    handleChange(value, direction, movedKeys) {
      this.selectedTags = value
    },
    handleAddTag() {
      this.addTagDialogVisible = true
    },
    handleAddTagDialogClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done()
        })
        .catch(_ => {})
    },
    handleAddTagConfirm() {
      this.$refs.addTagForm.validate(valid => {
        if (valid) {
          this.tags.push({
            id: this.tags.length + 1,
            name: this.addTagForm.name
          })
          this.addTagDialogVisible = false
          this.$message({
            type: "success",
            message: "新增成功"
          })
        }
      })
    },
    handleEditTag(tag) {
      this.editTagForm.name = tag.name
      this.editTagDialogVisible = true
    },
    handleEditTagDialogClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done()
        })
        .catch(_ => {})
    },
    handleEditTagConfirm() {
      this.$refs.editTagForm.validate(valid => {
        if (valid) {
          this.tags = this.tags.map(tag => {
            if (tag.id === this.selectedTags[0].id) {
              return {
                ...tag,
                name: this.editTagForm.name
              }
            }
            return tag
          })
          this.editTagDialogVisible = false
          this.$message({
            type: "success",
            message: "编辑成功"
          })
        }
      })
    },
    renderContent(h, option) {
      return (
        <span>
          {option.name}
          <el-button
            type="text"
            icon="el-icon-edit"
            onClick={(e) => {
              e.stopPropagation();
              this.handleEditTag(option);
            }}
          ></el-button>
        </span>
      );
    },
    saveForm() {
      this.$refs["formRef"].validate(valid => {
        if (valid) {
          console.log(this.tagsValue)
          this.tags.forEach(tag => {
            this.tagsValue.forEach(selectedTag => {
              if (tag.id === selectedTag) {
                this.form.tags.push(tag)
              }
            })
          })

          store.commit("scene/SET_SCENE", this.form)
          this.$message({
            type: "success",
            message: "保存成功"
          })
        }
      })
    },
    preview() {
      this.staticDialogVisible = true
    }
  }
})
</script>

<template>
  <div>
    <el-form :model="form" ref="formRef" label-width="120px">
      <el-form-item label="投资场景名" >
        <el-input v-model="form.sceneName" placeholder="请输入投资场景"></el-input>
      </el-form-item>
      <el-form-item label="投资场景主题">
        <el-select v-model="form.theme" placeholder="请选择" filterable allow-create>
          <el-option
            v-for="item in themes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="投资场景描述">
        <el-input type="textarea" v-model="form.description"></el-input>
      </el-form-item>
      <el-form-item label="投资场景标签">
        <el-transfer
          v-model="tagsValue"
          :data="tags"
          :props="{ key: 'id', label: 'name' }"
          :titles="['标签列表', '已选标签']"
          filterable
          :render-content="renderContent"
        >
          <!-- 左侧列表插槽 -->
          <template #left-footer>
            <el-button
              type="primary"
              size="mini"
              @click="handleAddTag"
              style="margin-left: 29%;"
            >
              新增标签
            </el-button>
          </template>
          <!-- 左侧列表每一项插槽 -->
          <template #left="{ option }">
            <span>{{ option.name }}</span>
            <el-button
              type="text"
              icon="el-icon-edit"
              @click.stop="handleEditTag(option)"
            ></el-button>
          </template>
        </el-transfer>

        <!-- 新增标签对话框 -->
        <el-dialog
          title="新增标签"
          append-to-body
          :visible.sync="addTagDialogVisible"
          width="30%"
          :before-close="handleAddTagDialogClose"
        >
          <el-form :model="addTagForm" ref="addTagFormRef" label-width="80px">
            <el-form-item label="标签名称" prop="name">
              <el-input v-model="addTagForm.name"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
        <el-button @click="addTagDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAddTagConfirm">确 定</el-button>
      </span>
        </el-dialog>

        <!-- 编辑标签对话框 -->
        <el-dialog
          append-to-body
          title="编辑标签"
          :visible.sync="editTagDialogVisible"
          width="30%"
          :before-close="handleEditTagDialogClose"
        >
          <el-form :model="editTagForm" ref="editTagForm" label-width="80px">
            <el-form-item label="标签名称" prop="name">
              <el-input v-model="editTagForm.name"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
        <el-button @click="editTagDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEditTagConfirm">确 定</el-button>
      </span>
        </el-dialog>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveForm">保存</el-button>
        <el-button type="primary" @click="preview">预览</el-button>
        <el-dialog
          title="静态信息预览"
          :visible.sync="staticDialogVisible"
          width="50%">
          <div class="preview" style="margin-left: 120px;">
            <h3 style="align-self: center;">场景名：{{ form.name }}</h3>
            <p>场景主题：{{ form.theme }}</p>
            <p>场景描述：
              <div v-html=" form.description "></div>
            </p>
            <p>场景标签：
              <el-tag v-for="(tag) in selectedTags" :key="tag.id">{{ tag.name }}</el-tag>
            </p>
          </div>
          <span slot="footer" class="dialog-footer">
        <el-button @click="staticDialogVisible = false">关闭</el-button>

      </span>
        </el-dialog>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped lang="scss">

</style>
