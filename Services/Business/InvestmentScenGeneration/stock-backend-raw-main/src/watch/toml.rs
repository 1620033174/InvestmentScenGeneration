use crate::init::file_len;

use super::Updater;
use std::collections::HashMap;
use std::fs;
use std::fs::File;
use std::io::Write;
use std::sync::Mutex;
use walkdir::WalkDir;

pub struct TomlConfig {
    pub files: Mutex<HashMap<String, String>>,
}

impl TomlConfig {
    fn get_files_hash(&self, path: &std::path::Path) -> HashMap<String, String> {
        let mut files: HashMap<String, String> = HashMap::new();
        for entry in WalkDir::new(path).into_iter().filter_map(|e| e.ok()) {
            if entry.path().is_file() {
                let val = sha256::try_digest(entry.path()).unwrap();
                files.insert(entry.path().to_str().unwrap().to_string(), val);
            }
        }
        files
    }

    fn set_files_hash(&self) {
        let mut output = File::create("Config.toml").expect("创建配置文件失败");
        let toml = toml::to_string(&self.files).expect("生成文件 hash 失败");
        write!(output, "{}", toml).expect("写配置文件失败");
    }

    // pub fn insert_files_hash(&mut self, path: &std::path::Path) {
    //     for entry in WalkDir::new(path).into_iter().filter_map(|e| e.ok()) {
    //         if entry.path().is_file() {
    //             self.files.insert(
    //                 entry.path().to_str().unwrap().to_string(),
    //                 sha256::try_digest(entry.path()).unwrap(),
    //             );
    //         }
    //     }
    //     self.set_files_hash();
    // }
}

// fn print_file(path: &str) {
//     let content = fs::read_to_string(path).unwrap();
//     println!("{}", content);
// }

impl Updater for TomlConfig {
    fn init_files(&mut self, path: &std::path::Path) {
        if std::path::Path::new("Config.toml").exists() && file_len("Config.toml") != 0 {
            return;
        }
        *self.files.lock().unwrap() = self.get_files_hash(path);
        self.set_files_hash();
    }

    fn changed_files(&mut self, path: &std::path::Path) -> Result<Vec<String>, std::io::Error> {
        if !std::path::Path::new("Config.toml").exists() || file_len("Config.toml") == 0 {
            let keys: Vec<String> = self.get_files_hash(path).keys().cloned().collect();
            self.init_files(path);
            return Ok(keys);
        }
        let mut changed: Vec<String> = Vec::new();
        let str = fs::read_to_string("Config.toml").expect("无法读取配置文件");
        let config_map: HashMap<String, String> = toml::from_str(&str).expect("解析配置文件失败");
        let mut files = self.files.lock().unwrap();
        *files = config_map;
        let current_files = self.get_files_hash(path);
        for (path, hash) in &current_files {
            if !files.contains_key(path) {
                changed.push(path.to_string());
            } else if files.get(path).unwrap().ne(hash) {
                changed.push(path.to_string());
                // print_file(path);
            }
            files.insert(path.to_string(), hash.to_string());
        }

        // for (path, _hash) in config_map {
        //     if !current_files.contains_key(&path) {
        //         changed.push(path)
        //     }
        // }
        // self.files = current_files;
        Ok(changed)
    }

    fn save_files(&mut self) {
        self.set_files_hash()
    }
}

#[cfg(test)]
mod test {
    use std::path::Path;

    use super::*;

    #[test]
    fn test_init_files() {
        TomlConfig {
            files: Mutex::new(HashMap::new()),
        }
        .init_files(Path::new("python-src"));
    }

    #[test]
    fn test_changed_files() {
        let changed = TomlConfig {
            files: Mutex::new(HashMap::new()),
        }
        .changed_files(Path::new("python-src"))
        .unwrap();
        println!("{:?}", changed);
    }

    #[test]
    fn set_hash() {
        let mut config = TomlConfig {
            files: Mutex::new(HashMap::new()),
        };
        let changed = config.changed_files(Path::new("python-src")).unwrap();

        println!("{:?}", changed);
        config.set_files_hash();
    }
}
