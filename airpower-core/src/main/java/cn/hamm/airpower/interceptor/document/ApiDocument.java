package cn.hamm.airpower.interceptor.document;

import cn.hamm.airpower.annotation.ReadOnly;
import cn.hamm.airpower.util.DictionaryUtil;
import cn.hamm.airpower.util.ReflectUtil;
import cn.hamm.airpower.validate.dictionary.Dictionary;
import cn.hamm.airpower.validate.phone.Phone;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * <h1>Api文档</h1>
 *
 * @author Hamm
 */
@Data
@Accessors(chain = true)
public class ApiDocument {
    /**
     * <h2>标题</h2>
     */
    private String title;

    /**
     * <h2>接口文档</h2>
     */
    private String document;

    /**
     * <h2>请求参数</h2>
     */
    private List<ApiRequestParam> requestParamList = new ArrayList<>();

    /**
     * <h2>输出接口文档</h2>
     *
     * @param response 响应
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    public static void writeApiDocument(HttpServletResponse response, Class<?> clazz, Method method) {
        ApiDocument apiDocument = new ApiDocument();
        String className = ReflectUtil.getDescription(clazz);
        String methodName = ReflectUtil.getDescription(method);
        apiDocument.setTitle(className + " " + methodName + " Api接口文档");

        apiDocument.setDocument(ReflectUtil.getDocument(method));

        apiDocument.setRequestParamList(getRequestParamList(clazz, method));

        String html = """
                <!DOCTYPE html>
                      <html>
                          <head>
                              <title>AirPower4J 接口文档</title>
                              <meta name="referrer" content="never">
                              <meta charset="UTF-8">
                              <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
                              <link rel="stylesheet" href="//at.alicdn.com/t/c/font_666204_kf3f5tzpd8f.css">
                              <link rel="stylesheet" href="//cdn.hamm.cn/css/element.css">
                              <link rel="icon" href="//cdn.hamm.cn/favicon.ico">
                              <style>
                              .dictionary{
                                  display: flex;
                                  flex-direction: row;
                                  align-items: center;
                                  min-width: 200px;
                              }
                              .dictionary .key{
                                  color: red;
                              }
                              .dictionary .label{
                                  flex: 1;
                                  width: 0;
                              }
                              .body{
                                  position: absolute;
                                  left: 10%;
                                  right: 10%;
                                  top: 0;
                                  bottom: 0;
                                  display: flex;
                                  flex-direction: column;
                                  padding: 40px;
                              }
                              .body .card{
                                  flex: 1;
                                  height: 0;
                                  overflow: hidden;
                                  overflow-y: auto;
                              }
                              .body .header{
                                  font-size: 24px;
                                  font-weight: bold;
                                  padding: 20px 0px;
                              }
                              .body .desc{
                                  font-size: 14px;
                                  color: #999;
                                  background: #f5f5f5;
                                  padding: 10px 20px;
                                  margin-bottom: 20px;
                                  border-radius: 8px;
                              }
                              h2 {
                                  font-size: 16px;
                                  font-weight: bold;
                                  margin-top: 40px;
                              }
                              .content{
                                  display: flex;
                                  flex-direction: row;
                                  align-items: center;
                              }
                              .content .method{
                                  background: #666;
                                  color: white;
                                  padding: 2px 8px;
                                  font-size: 12px;
                                  border-radius: 5px;
                              }
                              .content .url{
                                  margin-left: 20px;
                                  color: #159;
                              }
                              </style>
                          </head>
                          <body>
                              <div id="app" v-cloak>
                                  <div class="body">
                                      <div class="header">{{api.title}}</div>
                                      <div class="desc">{{api.document}}</div>
                                      <div class="card">
                                          <h2>请求方式</h2>
                                          <div class="content">
                                              <div class="method">POST</div> <div class="url">{{url}}</div>
                                          </div>
                                          <h2>请求参数</h2>
                                          <el-table class="table" stripe size="medium" :data="api.requestParamList" default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
                                              <el-table-column prop="name" label="参数Key" ></el-table-column>
                                              <el-table-column prop="description" label="参数名称" width="200"></el-table-column>
                                              <el-table-column prop="type" width="150" label="数据类型"></el-table-column>
                                              <el-table-column label="其他说明">
                                                  <template slot-scope="scope">
                                                      <el-tag size="small" v-if="scope.row.required">必填</el-tag>
                                                      <el-tag size="small" v-if="scope.row.phone">电话</el-tag>
                                                      <el-tag size="small" v-if="scope.row.email">邮箱</el-tag>
                                                      <el-dropdown v-if="scope.row.dictionary.length>0">
                                                          <el-tag size="small">字典</el-tag>
                                                          <el-dropdown-menu slot="dropdown">
                                                              <el-dropdown-item v-for="item in scope.row.dictionary">
                                                                  <div class="dictionary">
                                                                      <div class="label">{{item.label}}</div>
                                                                      <div class="key">{{item.key}}</div>
                                                                  </div>
                                                              </el-dropdown-item>
                                                          </el-dropdown-menu>
                                                      </el-dropdown>
                                                  </template>
                                              </el-table-column>
                                              <el-table-column prop="document" label="备注说明"></el-table-column>
                                          </el-table>
                                      </div>
                                  </div>
                              </div>
                          </body>
                          <script src="//cdn.hamm.cn/js/vue-2.6.10.min.js"></script>
                          <script src="//cdn.hamm.cn/js/axios.min.js"></script>
                          <script src="//cdn.hamm.cn/js/element.js"></script>
                          <script src="//cdn.hamm.cn/js/vue-clipboard.min.js"></script>
                          <script>
                          const json =
                          """
                + JSONUtil.toJsonStr(apiDocument) +
                """
                                   </script>
                                   <script>
                                   new Vue({
                                       el: '#app',
                                       data() {
                                           return {
                                               url: window.location.pathname,
                                               api: json,
                                           }
                                       },
                                       created() {
                                           console.log(this.api)
                                           window.document.title = this.api.title + " - AirPower4J"
                                           this.api.requestParamList.sort((a,b)=>{
                                                       if(a.required){
                                                           return -1;
                                                       }
                                                       if(b.required){
                                                           return 1;
                                                       }
                                                   })
                                       },
                                       updated() {},
                                       methods: {}
                                   });
                                   </script>
                               
                               </html>
                        """;
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(html);
        } catch (IOException ignored) {

        }
    }

    /**
     * <h2>获取请求参数</h2>
     *
     * @param clazz  类
     * @param method 方法
     * @return 请求参数列表
     */
    private static List<ApiRequestParam> getRequestParamList(Class<?> clazz, Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
            return new ArrayList<>();
        }
        Parameter parameter = parameters[0];
        // 取出第一个参数

        return buildApiRequestParamList(clazz, parameter);
    }

    /**
     * <h2>将参数构建成请求参数列表</h2>
     *
     * @param currentClass 当前类
     * @param parameter    参数
     * @return 请求参数列表
     */
    private static List<ApiRequestParam> buildApiRequestParamList(Class<?> currentClass, Parameter parameter) {
        List<ApiRequestParam> params = new ArrayList<>();
        RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
        if (Objects.isNull(requestBody)) {
            return params;
        }

        Class<?> action = Void.class;
        Validated validated = parameter.getAnnotation(Validated.class);
        if (Objects.nonNull(validated)) {
            if (validated.value().length == 0) {
                return params;
            }
            action = validated.value()[0];
        }

        Class<?> paramClass = parameter.getType();
        if (!parameter.getParameterizedType().getTypeName().equals(parameter.getType().getName())) {
            // 泛型
            paramClass = (Class<?>) ((((ParameterizedType) currentClass.getGenericSuperclass()).getActualTypeArguments())[0]);
        }

        for (Field field : ReflectUtil.getFieldList(paramClass)) {
            ReadOnly readOnly = field.getAnnotation(ReadOnly.class);
            if (Objects.nonNull(readOnly)) {
                continue;
            }

            ApiRequestParam apiRequestParam = new ApiRequestParam();
            apiRequestParam.setName(field.getName());
            apiRequestParam.setDescription(ReflectUtil.getDescription(field));
            apiRequestParam.setDocument(ReflectUtil.getDocument(field));
            apiRequestParam.setType(field.getType().getSimpleName());

            jakarta.validation.constraints.NotNull notNull = field.getAnnotation(jakarta.validation.constraints.NotNull.class);
            NotBlank notBlank = field.getAnnotation(NotBlank.class);

            if (!action.equals(Void.class)) {
                if (Objects.nonNull(notBlank) && Arrays.stream(notBlank.groups()).toList().contains(action)) {
                    apiRequestParam.setRequired(true);
                }
                if (Objects.nonNull(notNull) && Arrays.stream(notNull.groups()).toList().contains(action)) {
                    apiRequestParam.setRequired(true);
                }
            }

            Dictionary dictionary = field.getAnnotation(Dictionary.class);
            if (Objects.nonNull(dictionary) && Arrays.stream(dictionary.groups()).toList().contains(action)) {
                apiRequestParam.setDictionary(DictionaryUtil.getDictionaryList(dictionary.value()));
            }

            Phone phone = field.getAnnotation(Phone.class);
            if (Objects.nonNull(phone) && Arrays.stream(phone.groups()).toList().contains(action)) {
                if (phone.mobile() || phone.tel()) {
                    apiRequestParam.setPhone(true);
                }
            }


            Email email = field.getAnnotation(Email.class);
            if (Objects.nonNull(email) && Arrays.stream(email.groups()).toList().contains(action)) {
                apiRequestParam.setEmail(true);
            }
            params.add(apiRequestParam);
        }
        return params;
    }

    @Data
    @Accessors(chain = true)
    static class ApiRequestParam {
        private String name;
        private String type;
        private String description;
        private String document;
        private Boolean required = false;
        private List<Map<String, String>> dictionary = new ArrayList<>();
        private List<ApiRequestParam> children = new ArrayList<>();
        private Boolean phone = false;
        private Boolean email = false;
        private Integer maxLength = 0;
    }

}
