extern crate rand;

use std::io;
use rand::Rng;

fn generate(rows: u32, columns: u32) {
    println!("{} {}", rows, columns);
    for r in 0..rows {
        for c in 0..columns {
            let number = rand::thread_rng().gen_range(1, 101);
            print!("{} ", number);
        }
        println!("");
    }
}