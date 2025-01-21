export default function HamburgerButton({
  onClick,
  toggleMenuButton,
}: {
  onClick: () => void;
  toggleMenuButton: boolean;
}) {
  return (
    <div className="md:hidden">
      <button
        type="button"
        className={`z-40 block hamburger md:hidden ${
          toggleMenuButton ? "open" : ""
        }`}
        onClick={onClick}
      >
        <span className="hamburger-top"></span>
        <span className="hamburger-middle"></span>
        <span className="hamburger-bottom"></span>
      </button>
    </div>
  );
}
