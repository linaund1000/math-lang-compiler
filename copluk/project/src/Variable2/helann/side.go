package main

var Config struct {
    ServerAddress string
    Port          int
}

func init() {
    Config.ServerAddress = "localhost"
    Config.Port = 8080
}