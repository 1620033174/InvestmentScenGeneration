use std::fs;
use std::path::{Path, PathBuf};

pub fn get_contents_of_dir<P>(dir: P) -> Result<Vec<PathBuf>, std::io::Error>
where
    P: AsRef<Path>,
{
    fs::read_dir(dir)?
        .into_iter()
        .map(|x| x.map(|entry| entry.path()))
        .collect()
}

#[cfg(test)]
mod test {
    use super::*;
    #[test]
    fn test_get_contents_of_dir() {
        let result = get_contents_of_dir(std::path::Path::new("python-src"));
        println!("{result:?}");
    }

    #[test]
    fn test_get_contents_of_dir2() {
        let result = get_contents_of_dir(std::path::Path::new("python-src/get_selected_stocks"));
        println!("{result:?}");
    }

    #[test]
    fn test_get_contents_of_dir3() {
        let result = get_contents_of_dir(std::path::Path::new("cron-algorithm"));
        println!("{result:?}");
    }
}
