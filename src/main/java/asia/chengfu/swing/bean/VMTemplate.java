package asia.chengfu.swing.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class VMTemplate {
    private final int vmid;
    private final String name;

    @Override
    public String toString() {
        return this.name;
    }
}