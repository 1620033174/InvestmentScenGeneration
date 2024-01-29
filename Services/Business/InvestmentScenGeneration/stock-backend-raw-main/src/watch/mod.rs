pub mod toml;

use std::path::{Path};

pub trait Updater {
    fn init_files(&mut self, path: &Path);

    fn changed_files(&mut self, path: &Path) -> Result<Vec<String>, std::io::Error>;

    fn save_files(&mut self);
}
