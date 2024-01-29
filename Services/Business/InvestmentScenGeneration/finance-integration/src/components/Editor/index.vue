<template>
  <div style="border: 1px solid #ccc">
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editor"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    />
    <Editor
      v-model="html"
      style="height: 300px"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="onCreated"
      @customPaste="customPaste"
      @onChange="onChange"
    />
  </div>
</template>

<script>
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { DomEditor } from "@wangeditor/editor";
import { Boot, IEditorConfig, IToolbarConfig } from "@wangeditor/editor";
import formulaModule from "@wangeditor/plugin-formula";

// 注册。要在创建编辑器之前注册，且只能注册一次，不可重复注册。
Boot.registerModule(formulaModule);
export default {
  components: { Editor, Toolbar },
  props: {
    editorContent: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      editor: null,
      html: this.editorContent,
      toolbarConfig: {
        excludeKeys: [],
        insertKeys: {
          index: 0,
          keys: [],
        },
        modalAppendToBody: false,
        toolbarKeys: [
          // TODO: 自定义插入一个数学符号
          "sup",
          "sub",
          "clearStyle",
          "|",
          "insertFormula",
          "|",
          "undo",
          "redo",
          "fullScreen",
          // {
          //   key: "group-image",
          //   title: "图片",
          //   iconSvg:
          //     '<svg viewBox="0 0 1024 1024"><path d="M959.877 128l0.123 0.123v767.775l-0.123 0.122H64.102l-0.122-0.122V128.123l0.122-0.123h895.775zM960 64H64C28.795 64 0 92.795 0 128v768c0 35.205 28.795 64 64 64h896c35.205 0 64-28.795 64-64V128c0-35.205-28.795-64-64-64zM832 288.01c0 53.023-42.988 96.01-96.01 96.01s-96.01-42.987-96.01-96.01S682.967 192 735.99 192 832 234.988 832 288.01zM896 832H128V704l224.01-384 256 320h64l224.01-192z"></path></svg>',
          //   menuKeys: ["insertImage", "uploadImage"],
          // },
        ],
      },
      editorConfig: {
        placeholder: "请输入内容...",
      },
      mode: "default", // or 'simple'
    };
  },
  methods: {
    onCreated(editor) {
      this.editor = Object.seal(editor); // 一定要用 Object.seal() ，否则会报错
      // const toolbar = DomEditor.getToolbar(editor);
      // const curToolbarConfig = toolbar.getConfig();
      // console.log(curToolbarConfig.toolbarKeys); // 当前菜单排序和分组
    },
    customPaste(editor, event) {
      // editorConfig.customPaste = (editor, event) => {                                       // JS 语法

      // event 是 ClipboardEvent 类型，可以拿到粘贴的数据
      // 可参考 https://developer.mozilla.org/zh-CN/docs/Web/API/ClipboardEvent

      // const html = event.clipboardData.getData('text/html') // 获取粘贴的 html
      // const text = event.clipboardData.getData('text/plain') // 获取粘贴的纯文本
      // const rtf = event.clipboardData.getData('text/rtf') // 获取 rtf 数据（如从 word wsp 复制粘贴）
      const text = event.clipboardData.getData("text/plain");
      editor.insertText(text);
      /*   异步
      setTimeout(() => {
        editor.insertText("yy");
      }, 1000); */
      // 阻止默认的粘贴行为
      event.preventDefault();
      return false;
    },
    onChange(editor) {
      const html = editor.getHtml();
      this.$emit("changeEditorContent", html);
    },
  },
  updated() {
    // NOTE: 暂时用来看wangeditor toolbar的默认配置,updated的具体使用方式之后再考虑
    // const html = this.editor.getHtml();
    // console.log("html", html);
    // const toolbar = DomEditor.getToolbar(this.editor);
    // const curToolbarConfig = toolbar.getConfig();
    // console.log("keys", this.editor.getAllMenuKeys());
  },
  mounted() {
    // 模拟 ajax 请求，异步渲染编辑器
    // setTimeout(() => {
    //   this.html = this.editorState.content;
    // }, 1500);
  },
  beforeDestroy() {
    const editor = this.editor;
    if (editor == null) return;
    editor.destroy(); // 组件销毁时，及时销毁编辑器
  },
};
</script>
<style src="@wangeditor/editor/dist/css/style.css"></style>
