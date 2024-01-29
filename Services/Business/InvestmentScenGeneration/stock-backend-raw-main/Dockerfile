FROM rust:1.66 as builder
WORKDIR /usr/src/stock-backend
COPY ./requirements.txt .
RUN sed -i s/deb.debian.org/mirrors.aliyun.com/g /etc/apt/sources.list && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
RUN apt-get update && apt-get install -y python3-dev locales locales-all python3-pip && pip install -i https://pypi.tuna.tsinghua.edu.cn/simple -r /usr/src/stock-backend/requirements.txt && rm -rf /var/lib/apt/lists/*


# for cache
RUN echo "fn main() {}" > dummy.rs
COPY Cargo.toml .
COPY Cargo.lock .
COPY .cargo ./.cargo
RUN mkdir src && echo 'fn main() { println!("Dummy!"); }' > ./src/main.rs
RUN cargo build --release
RUN rm -rf ./src

COPY . .
RUN touch -a -m ./src/main.rs
ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8
ENV RUST_LOG=INFO
ENV CONN=mongodb://sjj-mongodb:27017
RUN cargo build --release && cp target/release/stock-backend . && rm -rf ./target

CMD ["./stock-backend"]

EXPOSE 80