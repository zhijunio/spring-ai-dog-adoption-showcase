# Spring AI 狗狗领养助手展示项目

一个基于 Spring AI 的智能狗狗领养助手系统，展示了 RAG（检索增强生成）、工具调用、聊天记忆等核心 AI 能力。

## 文章

- [零基础实战：用 Spring AI 写一个会“思考”的狗狗领养助手](https://blog.chensoul.cc/posts/2025/11/13/spring-ai-dog-adoption-showcase/) - 使用 RAG、MCP、向量存储和 PgVector 或者 PostgreSQL 构建可用于生产的狗狗领养服务的完整教程。

## 如何运行

```bash
git clone https://github.com/zhijunio/spring-ai-dog-adoption-showcase
cd spring-ai-dog-adoption-showcase

cd scheduler
./mvnw spring-boot:run

cd ../adoptions
docker-compose up -d postgres

export OPENAI_API_KEY=your-openai-api-key
./mvnw spring-boot:run
```

## 测试

```bash
# Search for adoptable dogs
curl -G "http://localhost:8080/alice/assistant" --data-urlencode "question=do you have any neurotic dogs?"

# Schedule an appointment
curl -G "http://localhost:8080/alice/assistant" --data-urlencode "question=I think Prancer is cool, help me schedule an appointment to adopt it"
```